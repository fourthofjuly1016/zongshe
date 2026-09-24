package com.example.demo.transport.strategy;

import com.example.demo.transport.entity.Need;
import com.example.demo.transport.entity.Vehicle;
import org.springframework.stereotype.Component;

@Component
public class SimpleMatchStrategy implements MatchStrategy {

    @Override
    public double score(Need need, Vehicle vehicle) {
        if (vehicle.getVehicleWeight() == null) {
            return 0;
        }

        // 基础分
        double score = 50;

        // 空闲车辆加分
        if ("IDLE".equals(vehicle.getVehicleStatus())) {
            score += 30;
        }

        // 距离分（暂时用固定值，后续可以按经纬度算）
        score += 20;

        return score;
    }
}