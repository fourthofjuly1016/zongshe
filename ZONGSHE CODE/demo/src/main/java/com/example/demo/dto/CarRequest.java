package com.example.demo.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CarRequest(
        @NotBlank(message = "车牌号不能为空")
        @Size(max = 32, message = "车牌号不能超过32个字符")
        String plateNumber,
        @NotBlank(message = "车辆状态不能为空")
        @Size(max = 32, message = "车辆状态不能超过32个字符")
        String carStatus,
        @NotBlank(message = "车辆类型不能为空")
        @Size(max = 64, message = "车辆类型不能超过64个字符")
        String carType,
        @NotNull(message = "货位数不能为空")
        @PositiveOrZero(message = "货位数不能小于0")
        Integer carStorage,
        @NotNull(message = "最大载重不能为空")
        @DecimalMin(value = "0.0", message = "最大载重不能小于0")
        BigDecimal maxWeight,
        @NotNull(message = "最大容积不能为空")
        @DecimalMin(value = "0.0", message = "最大容积不能小于0")
        BigDecimal maxVolume
) {
}
