package com.example.demo.dto;

import java.math.BigDecimal;

public record CarResponse(
        Integer carId,
        String plateNumber,
        String carStatus,
        String carType,
        Integer carStorage,
        BigDecimal maxWeight,
        BigDecimal maxVolume,
        Boolean deleted
) {
}
