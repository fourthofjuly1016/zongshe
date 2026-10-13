package com.example.demo.config;

import com.example.demo.entity.Vehicle;
import com.example.demo.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class SimulationLoop {

    private final VehicleRepository vehicleRepository;

    /**
     * 仿真主循环：每 5 秒执行一次
     */
    @Scheduled(fixedRate = 5000)
    public void mainLoop() {
        System.out.println("===== 仿真主循环执行 =====");

        // 1. 查询所有 BUSY 状态的车辆
        List<Vehicle> vehicles = vehicleRepository.findByVehicleStatus("BUSY");

        // 2. 模拟车辆移动（经纬度随机变化）
        for (Vehicle v : vehicles) {
            if (v.getCurrentLat() != null && v.getCurrentLng() != null) {
                double newLat = v.getCurrentLat() + (Math.random() - 0.5) * 0.01;
                double newLng = v.getCurrentLng() + (Math.random() - 0.5) * 0.01;
                v.setCurrentLat(newLat);
                v.setCurrentLng(newLng);
                vehicleRepository.save(v);
                System.out.println("车辆 " + v.getVehicleId()
                        + " 移动到 [" + newLat + ", " + newLng + "]");
            }
        }

        System.out.println("===== 仿真主循环结束 =====");
    }
}