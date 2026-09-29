package com.example.demo.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record PoiRequest(
        @NotBlank(message = "POI名称不能为空")
        @Size(max = 100, message = "POI名称不能超过100个字符")
        String name,
        @NotBlank(message = "POI类型不能为空")
        @Size(max = 32, message = "POI类型不能超过32个字符")
        String type,
        @NotBlank(message = "POI地址不能为空")
        @Size(max = 255, message = "POI地址不能超过255个字符")
        String address,
        @NotNull(message = "纬度不能为空")
        @DecimalMin(value = "-90", message = "纬度不能小于-90")
        @DecimalMax(value = "90", message = "纬度不能大于90")
        BigDecimal latitude,
        @NotNull(message = "经度不能为空")
        @DecimalMin(value = "-180", message = "经度不能小于-180")
        @DecimalMax(value = "180", message = "经度不能大于180")
        BigDecimal longitude,
        @NotNull(message = "停留时间不能为空")
        @PositiveOrZero(message = "停留时间不能小于0")
        Integer stayTime
) {
}
