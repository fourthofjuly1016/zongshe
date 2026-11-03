package com.example.demo.service;

import com.example.demo.entity.Factory;
import com.example.demo.entity.Need;
import com.example.demo.entity.Vehicle;
import com.example.demo.enums.VehicleState;
import com.example.demo.repository.FactoryRepository;
import com.example.demo.repository.NeedRepository;
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
    private final NeedRepository needRepository;
    // ★ 这里就是你截图里说的“注入”，我已经写好了
    private final FactoryRepository factoryRepository; 

    private static final int TICK_SIMULATION_MINUTES = 5;
    

    @Scheduled(fixedRate = 5000) // 每 5 秒触发一次
    public void runSimulationTick() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        
        for (Vehicle vehicle : vehicles) {
            
            // 1. 如果是空闲状态，尝试派单
            if ("IDLE".equals(vehicle.getVehicleStatus())) {
                List<Need> validNeeds = needRepository.findByNeedStatus("VALID");
                if (!validNeeds.isEmpty()) {
                    Need taskNeed = validNeeds.get(0);
                    taskNeed.setNeedStatus("ASSIGNED");
                    needRepository.save(taskNeed);

                    // 找个工厂作为装货地
                    List<Factory> factories = factoryRepository.findAll();
                    Factory targetFactory = factories.isEmpty() ? null : factories.get(0);

                    vehicle.setVehicleStatus(VehicleState.TO_PICKUP.name());
                    vehicle.setCurrentPhaseElapsed(0);
                    
                    // 目标点变成工厂的位置
                    if (targetFactory != null) {
                        vehicle.setTargetLat(targetFactory.getFactoryLat());
                        vehicle.setTargetLng(targetFactory.getFactoryLng());
                        System.out.println(">>> 车辆 " + vehicle.getVehicleId() + " 接单，前往 " + targetFactory.getFactoryName() + " 装货！");
                    } else {
                        vehicle.setTargetLat(vehicle.getCurrentLat() + 0.02);
                        vehicle.setTargetLng(vehicle.getCurrentLng() + 0.02);
                    }
                    vehicleRepository.save(vehicle);
                }
                continue; 
            }

            // 2. 推进时间与状态机
            VehicleState currentState;
            try {
                currentState = VehicleState.valueOf(vehicle.getVehicleStatus());
            } catch (Exception e) {
                continue; 
            }

            int elapsed = vehicle.getCurrentPhaseElapsed() != null ? vehicle.getCurrentPhaseElapsed() : 0;
            int total = getTotalTimeForState(vehicle, currentState);
            
            elapsed += TICK_SIMULATION_MINUTES;

            if (elapsed < total) {
                // 【阶段未完成】：更新耗时、更新位置
                vehicle.setCurrentPhaseElapsed(elapsed);
                if (currentState == VehicleState.TO_PICKUP || currentState == VehicleState.TRANSPORTING) {
                    moveVehicle(vehicle);
                }
                vehicleRepository.save(vehicle);
            } else {
                // 【阶段已完成】：触发马尔科夫转换
                VehicleState nextState = MarkovChain.chooseNextState(currentState);
                
                // ================= 核心业务逻辑 =================
                if (currentState == VehicleState.UNLOADING) {
                    // 卸货完成，需求运抵
                    List<Need> assignedNeeds = needRepository.findByNeedStatus("ASSIGNED");
                    if (!assignedNeeds.isEmpty()) {
                        Need finishedNeed = assignedNeeds.get(0);
                        finishedNeed.setNeedStatus("ARRIVED");
                        needRepository.save(finishedNeed);
                        System.out.println(">>> 需求 " + finishedNeed.getNeedId() + " 已成功运抵！");
                    }
                } else if (currentState == VehicleState.LOADING && nextState == VehicleState.TRANSPORTING) {
                    // ★ 你刚才问的代码在这里：装货完成，设定随机送货目的地
                    vehicle.setTargetLat(vehicle.getCurrentLat() + (Math.random() - 0.5) * 0.1);
                    vehicle.setTargetLng(vehicle.getCurrentLng() + (Math.random() - 0.5) * 0.1);
                    System.out.println(">>> 车辆装货完毕，开始前往目的地送货！");
                }
                // ===============================================
                
                System.out.println(">>> 车辆 " + vehicle.getVehicleId() + " 状态转换：" + currentState + " -> " + nextState);
                
                vehicle.setVehicleStatus(nextState.name());
                vehicle.setCurrentPhaseElapsed(0); 
                
                // 业务回调
                if (nextState == VehicleState.TO_PICKUP || nextState == VehicleState.LOADING) {
                    selectGoods(vehicle);
                } else if (nextState == VehicleState.WAITING || nextState == VehicleState.IDLE) {
                    WaitAndSearchNewNeed(vehicle);
                }
                
                vehicleRepository.save(vehicle);
            }
        }
    }

    // 选货逻辑：装货时扣减货物
    private void selectGoods(Vehicle vehicle) {
        System.out.println(">>> 车辆 " + vehicle.getVehicleId() + " 调用 selectGoods() 寻找货源...");
        List<Need> validNeeds = needRepository.findByNeedStatus("VALID");
        
        if (!validNeeds.isEmpty()) {
            Need targetNeed = validNeeds.get(0);
            double loadAmount = 30.0; 
            double currentRemaining = targetNeed.getRemainingQuantity() != null ? targetNeed.getRemainingQuantity() : 100.0;
            
            if (currentRemaining > 0) {
                double newRemaining = currentRemaining - loadAmount;
                if (newRemaining <= 0) {
                    newRemaining = 0;
                    targetNeed.setNeedStatus("ASSIGNED");
                    System.out.println(">>> 货物已全部装车！需求ID: " + targetNeed.getNeedId() + " 剩余: 0吨");
                } else {
                    System.out.println(">>> 部分装车！需求ID: " + targetNeed.getNeedId() + " 扣减了 " + loadAmount + " 吨，剩余: " + newRemaining + " 吨");
                }
                targetNeed.setRemainingQuantity(newRemaining);
                needRepository.save(targetNeed);
            }
        } else {
            System.out.println(">>> 没找到货物，车辆进入空闲状态");
            vehicle.setVehicleStatus(VehicleState.IDLE.name());
        }
    }

    // 等待找新单逻辑
    private void WaitAndSearchNewNeed(Vehicle vehicle) {
        System.out.println(">>> 车辆 " + vehicle.getVehicleId() + " 调用 WaitAndSearchNewNeed() 等待/找新单...");
        vehicle.setVehicleStatus(VehicleState.IDLE.name());
        vehicle.setCurrentPhaseElapsed(0);
    }

    // 获取阶段总耗时
    private int getTotalTimeForState(Vehicle vehicle, VehicleState state) {
        switch (state) {
            case TO_PICKUP: return 20;
            case LOADING: return 60;   
            case TRANSPORTING: return 100; 
            case UNLOADING: return 60; 
            case WAITING: return 5;    
            default: return 0;
        }
    }

    // 车辆位置移动计算
    private void moveVehicle(Vehicle vehicle) {
        if (vehicle.getCurrentLat() == null || vehicle.getTargetLat() == null) return;
        
        double dLat = vehicle.getTargetLat() - vehicle.getCurrentLat();
        double dLng = vehicle.getTargetLng() - vehicle.getCurrentLng();
        
        // 每次移动剩余距离的 30%（为了可视效果好一点，实际应用中应按真实速度计算）
        double newLat = vehicle.getCurrentLat() + dLat * 0.3;
        double newLng = vehicle.getCurrentLng() + dLng * 0.3;
        
        vehicle.setCurrentLat(newLat);
        vehicle.setCurrentLng(newLng);
    }
}