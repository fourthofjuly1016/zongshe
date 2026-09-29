package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record VehiclePositionResponse(
        Long positionId,
        Integer carId,
        String plateNumber,
        BigDecimal latitude,
        BigDecimal longitude,
        BigDecimal speed,
        BigDecimal heading,
        Boolean gpsValid,
        LocalDateTime collectedAt,
        String source
) {
}
