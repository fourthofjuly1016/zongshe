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

@Component
@EnableScheduling
@RequiredArgsConstructor
public class SimulationEngine {

    private final VehicleRepository vehicleRepository;

    // 现实1s = 仿真1min？文档说5s代表5min
    private static final int TICK_SIMULATION_MINUTES = 5; 
    private static final double SPEED_KM_PER_MINUTE = 1.0; // 60km/h 即 1km/min

    @Scheduled(fixedRate = 5000) // 每 5 秒触发一次
    public void runSimulationTick() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        
        for (Vehicle vehicle : vehicles) {
            // 1. 如果是空闲状态，尝试派单（此处简化，假设已经从数据库拿到了状态）
            if ("IDLE".equals(vehicle.getVehicleStatus())) {
                // TODO: 匹配逻辑（省略），如果有任务，把状态改为 TO_PICKUP
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
                
                // 业务校验：如果是运货行驶，必须转卸货（按文档要求）
                // TODO: 这里可以加硬性约束，比如 nextState == TRANSPORTING 但没有货物，强制变 UNLOADING
                
                vehicle.setVehicleStatus(nextState.name());
                vehicle.setCurrentPhaseElapsed(0); // 新阶段耗时清零
                
                // 初始化新阶段的时间
                vehicle.setCurrentPhaseElapsed(0);
                // (此处省略：设置新阶段的目标位置、总耗时等，根据 nextState 来)
                
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
}