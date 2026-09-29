package com.example.demo.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.Objects;

public record RoadRequest(
        @NotBlank(message = "道路名称不能为空")
        @Size(max = 100, message = "道路名称不能超过100个字符")
        String name,
        @NotNull(message = "起点POI不能为空")
        @Positive(message = "起点POI编号必须大于0")
        Integer startPoiId,
        @NotNull(message = "终点POI不能为空")
        @Positive(message = "终点POI编号必须大于0")
        Integer endPoiId,
        @NotNull(message = "道路距离不能为空")
        @DecimalMin(value = "0.01", message = "道路距离必须大于0")
        BigDecimal distance,
        @NotBlank(message = "道路状态不能为空")
        @Size(max = 32, message = "道路状态不能超过32个字符")
        String roadStatus,
        @NotBlank(message = "交通密度不能为空")
        @Size(max = 32, message = "交通密度不能超过32个字符")
        String trafficDensity,
        @NotNull(message = "危险等级不能为空")
        @DecimalMin(value = "0", message = "危险等级不能小于0")
        @DecimalMax(value = "10", message = "危险等级不能大于10")
        BigDecimal dangerousLevel,
        @NotNull(message = "通行费不能为空")
        @DecimalMin(value = "0", message = "通行费不能小于0")
        BigDecimal tollFee
) {
    @AssertTrue(message = "道路起点和终点不能相同")
    public boolean isDifferentEndpoints() {
        return !Objects.equals(startPoiId, endPoiId);
    }
}
