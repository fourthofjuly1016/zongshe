package com.example.demo.service;

import com.example.demo.dto.MatchingScoreResponse;
import com.example.demo.entity.Car;
import com.example.demo.entity.TransportTask;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class MatchingService {
    public MatchingScoreResponse score(TransportTask task, Car car) {
        List<String> reasons = new ArrayList<>();
        BigDecimal score = BigDecimal.ZERO;
        boolean eligible = true;
        if (Boolean.TRUE.equals(car.getDeleted()) || "INVALID".equalsIgnoreCase(car.getCarStatus())) {
            eligible = false;
            reasons.add("车辆已失效");
        } else if (!"IDLE".equalsIgnoreCase(car.getCarStatus())) {
            eligible = false;
            reasons.add("车辆当前不在空闲状态");
        } else {
            score = score.add(BigDecimal.valueOf(25));
        }

        BigDecimal requiredWeight = task.getGoods().getGoodsWeight().multiply(task.getQuantity());
        BigDecimal requiredVolume = task.getGoods().getGoodsVolume().multiply(task.getQuantity());
        if (car.getMaxWeight().compareTo(requiredWeight) < 0) {
            eligible = false;
            reasons.add("车辆载重不足");
        } else {
            score = score.add(BigDecimal.valueOf(25));
        }
        if (car.getMaxVolume().compareTo(requiredVolume) < 0) {
            eligible = false;
            reasons.add("车辆容积不足");
        } else {
            score = score.add(BigDecimal.valueOf(20));
        }

        boolean dangerous = Boolean.TRUE.equals(task.getGoods().getGoodsIsdangerous());
        boolean dangerousVehicle = car.getCarType().contains("危险") || car.getCarType().toUpperCase().contains("DANGER");
        if (dangerous && !dangerousVehicle) {
            eligible = false;
            reasons.add("危险品需求需要危险品车辆");
        } else {
            score = score.add(BigDecimal.valueOf(15));
        }
        if (car.getCarType().equalsIgnoreCase(task.getGoods().getCategory())
                || car.getCarType().equalsIgnoreCase("VAN")
                || car.getCarType().equalsIgnoreCase("GENERAL")) {
            score = score.add(BigDecimal.valueOf(10));
            reasons.add("车型与货物类别匹配");
        }
        score = score.add(BigDecimal.valueOf(Math.max(0, 5 - task.getPriority())));
        if (reasons.isEmpty()) {
            reasons.add("车辆满足基础容量与状态规则");
        }
        return new MatchingScoreResponse(task.getTaskId(), car.getCarId(),
                score.min(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP), eligible, reasons);
    }
}
