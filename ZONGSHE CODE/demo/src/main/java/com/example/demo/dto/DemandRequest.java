package com.example.demo.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DemandRequest(
        @NotNull(message = "货物不能为空") @Positive(message = "货物编号必须大于0") Integer goodsId,
        @NotNull(message = "装货点不能为空") @Positive(message = "装货点编号必须大于0") Integer pickupPoiId,
        @NotNull(message = "卸货点不能为空") @Positive(message = "卸货点编号必须大于0") Integer deliveryPoiId,
        @NotNull(message = "需求数量不能为空") @DecimalMin(value = "0.01", message = "需求数量必须大于0") BigDecimal quantity,
        @NotNull(message = "优先级不能为空") @Min(1) @Max(5) Integer priority,
        LocalDateTime plannedPickupTime,
        LocalDateTime plannedDeliveryTime
) {
}
