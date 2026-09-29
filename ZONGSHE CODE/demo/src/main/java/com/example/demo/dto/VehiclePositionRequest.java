package com.example.demo.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record VehiclePositionRequest(
        @NotNull(message = "纬度不能为空") @DecimalMin("-90") @DecimalMax("90") BigDecimal latitude,
        @NotNull(message = "经度不能为空") @DecimalMin("-180") @DecimalMax("180") BigDecimal longitude,
        @PositiveOrZero(message = "速度不能小于0") BigDecimal speed,
        @DecimalMin("0") @DecimalMax("360") BigDecimal heading,
        Boolean gpsValid,
        LocalDateTime collectedAt,
        String source
) {
}
