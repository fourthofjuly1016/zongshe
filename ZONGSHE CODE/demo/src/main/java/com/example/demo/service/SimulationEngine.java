package com.example.demo.service;

import com.example.demo.entity.Vehicle;
import com.example.demo.enums.VehicleState;
import com.example.demo.repository.VehicleRepository;
import com.example.demo.util.MarkovChain;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import com.example.demo.entity.Need;
import com.example.demo.repository.NeedRepository;



@Component
@EnableScheduling
@RequiredArgsConstructor
public class SimulationEngine {

    private final VehicleRepository vehicleRepository;

    // 现实1s = 仿真1min
    private static final int TICK_SIMULATION_MINUTES = 5; 
    private static final double SPEED_KM_PER_MINUTE = 1.0; // 60km/h 即 1km/min

    @Scheduled(fixedRate = 5000) // 每 5 秒触发一次
    public void runSimulationTick() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        
        for (Vehicle vehicle : vehicles) {
       // 1. 如果是空闲状态，尝试自动派单
           if ("IDLE".equals(vehicle.getVehicleStatus())) {
    // 查一下数据库里有没有等待运输的需求
            List<Need> validNeeds = needRepository.findByNeedStatus("VALID");
              if (!validNeeds.isEmpty()) {
            Need taskNeed = validNeeds.get(0);
             // 锁定这个需求，防止被别的车抢走
            taskNeed.setNeedStatus("ASSIGNED");
            needRepository.save(taskNeed);

             // 把车辆状态改为接单行驶
            vehicle.setVehicleStatus(VehicleState.TO_PICKUP.name());
            vehicle.setCurrentPhaseElapsed(0);
        
             // 模拟设置一个目标点（这里给个随机偏移坐标，实际中应取需求的起运地坐标）
            vehicle.setTargetLat(vehicle.getCurrentLat() + (Math.random() - 0.5) * 0.05);
            vehicle.setTargetLng(vehicle.getCurrentLng() + (Math.random() - 0.5) * 0.05);
        
            System.out.println(">>> 车辆 " + vehicle.getVehicleId() + " 自动接单成功！进入 TO_PICKUP 状态");
            vehicleRepository.save(vehicle);
            }
            continue; 
        }

            // 2. 状态推进逻辑
            VehicleState currentState;
            try {
                currentState = VehicleState.valueOf(vehicle.getVehicleStatus());
            } catch (Exception e) {
                continue; // 状态不合法跳过
            }

            // 如果当前阶段没结束，累加时间并计算位置
            int elapsed = vehicle.getCurrentPhaseElapsed() != null ? vehicle.getCurrentPhaseElapsed() : 0;
            int total = getTotalTimeForState(vehicle, currentState);
            
            elapsed += TICK_SIMULATION_MINUTES;

            if (elapsed < total) {
                // 【阶段未完成】：更新耗时、更新位置（如果是行驶状态）
                vehicle.setCurrentPhaseElapsed(elapsed);
                if (currentState == VehicleState.TO_PICKUP || currentState == VehicleState.TRANSPORTING) {
                    moveVehicle(vehicle);
                }
                vehicleRepository.save(vehicle);
          } else {
    // 【阶段已完成】：触发马尔科夫转换
    VehicleState nextState = MarkovChain.chooseNextState(currentState);
    
    // ================= 新增：需求状态同步更新 =================
    // 如果车辆完成了卸货，并且下一个状态是空闲（或者等待）
    if (currentState == VehicleState.UNLOADING) {
        // 找到这辆车最近绑定的、处于 ASSIGNED 状态的需求
        List<Need> assignedNeeds = needRepository.findByNeedStatus("ASSIGNED");
        if (!assignedNeeds.isEmpty()) {
            Need finishedNeed = assignedNeeds.get(0);
            finishedNeed.setNeedStatus("ARRIVED"); // 标记为已运抵
            needRepository.save(finishedNeed);
            System.out.println(">>> 需求 " + finishedNeed.getNeedId() + " 已成功运抵！");
        }
    }
    // =========================================================
    
    System.out.println(">>> 车辆 " + vehicle.getVehicleId() + " 状态转换：" + currentState + " -> " + nextState);
    vehicle.setVehicleStatus(nextState.name());
    vehicle.setCurrentPhaseElapsed(0);
    

    vehicle.setCurrentPhaseElapsed(0);
    
           vehicle.setCurrentPhaseElapsed(0);

           // ================= 新增的业务调用 =================
           if (nextState == VehicleState.TO_PICKUP || nextState == VehicleState.LOADING) {
           // 如果准备去装货或接单，去执行找货逻辑
            selectGoods(vehicle);
            } else if (nextState == VehicleState.WAITING || nextState == VehicleState.IDLE) {
             // 如果进入等待或空闲，调用找新需求逻辑
             WaitAndSearchNewNeed(vehicle);
         }
          // =================================================
    
             vehicleRepository.save(vehicle);
}
        }
    }

    // 获取当前阶段总耗时
    private int getTotalTimeForState(Vehicle vehicle, VehicleState state) {
        switch (state) {
            case TO_PICKUP: return 20; // 假设 20 分钟到达
            case LOADING: return 60;   // 装货固定 60 分钟
            case TRANSPORTING: return 100; // 运货行驶（按距离算）
            case UNLOADING: return 60; // 卸货固定 60 分钟
            case WAITING: return 5;    // 等待 5 分钟
            default: return 0;
        }
    }

    // 根据速度和每 tick 的时间，让车辆向目标点移动
    private void moveVehicle(Vehicle vehicle) {
        if (vehicle.getCurrentLat() == null || vehicle.getTargetLat() == null) return;
        
        // 每 tick 行驶距离 (km)
        double distancePerTick = SPEED_KM_PER_MINUTE * TICK_SIMULATION_MINUTES; 
        
        // 简化的经纬度移动逻辑（实际中需按球面距离计算，这里用线性插值简化演示）
        // 假设向东/向北移动
        double dLat = vehicle.getTargetLat() - vehicle.getCurrentLat();
        double dLng = vehicle.getTargetLng() - vehicle.getCurrentLng();
        
        // 简化的移动量（仅做演示，具体移动量需按比例计算）
        double newLat = vehicle.getCurrentLat() + dLat * 0.1;
        double newLng = vehicle.getCurrentLng() + dLng * 0.1;
        
        vehicle.setCurrentLat(newLat);
        vehicle.setCurrentLng(newLng);
    }


    private final NeedRepository needRepository;


    /**
 * 提示1：选货逻辑。当车辆状态变为接单或装货时，给它分配一个具体需求
 */
    private void selectGoods(Vehicle vehicle) {
      System.out.println(">>> 车辆 " + vehicle.getVehicleId() + " 调用 selectGoods() 寻找货源...");
    
    // 简单实现：从数据库随机找一个有效需求（或者暂时假装给它派了一单）
    // 在完整版本中，这里应该结合你之前写的 MatchingService 匹配算法
    List<Need> validNeeds = needRepository.findByNeedStatus("VALID");
    
    if (!validNeeds.isEmpty()) {
        Need targetNeed = validNeeds.get(0); // 简单取第一个
        System.out.println(">>> 成功绑定需求: " + targetNeed.getCargoName());
        // 这里可以给 vehicle 设置目标的经纬度（起运地），这里暂时省略具体坐标映射
    } else {
        System.out.println(">>> 没找到货物，车辆进入空闲状态");
        vehicle.setVehicleStatus(VehicleState.IDLE.name());
    }
}

/**
 * 提示2：等待并寻找新需求。
 */
    private void WaitAndSearchNewNeed(Vehicle vehicle) {
      System.out.println(">>> 车辆 " + vehicle.getVehicleId() + " 调用 WaitAndSearchNewNeed() 等待/找新单...");
    
    // 简单实现：给它置为 IDLE，下个 tick 会重新走调度流程
       vehicle.setVehicleStatus(VehicleState.IDLE.name());
       vehicle.setCurrentPhaseElapsed(0);
}
}