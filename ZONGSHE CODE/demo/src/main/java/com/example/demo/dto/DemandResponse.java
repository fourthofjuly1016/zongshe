package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DemandResponse(
        Long taskId,
        String taskNo,
        Integer goodsId,
        String goodsName,
        Integer pickupPoiId,
        String pickupPoiName,
        Integer deliveryPoiId,
        String deliveryPoiName,
        BigDecimal quantity,
        Integer priority,
        String taskStatus,
        String source,
        Boolean deleted,
        LocalDateTime plannedPickupTime,
        LocalDateTime plannedDeliveryTime,
        LocalDateTime createdAt
) {
}
