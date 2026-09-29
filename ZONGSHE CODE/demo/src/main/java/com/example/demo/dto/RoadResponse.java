package com.example.demo.dto;

import java.math.BigDecimal;

public record RoadResponse(
        Integer roadId,
        String name,
        Integer startPoiId,
        String startPoiName,
        Integer endPoiId,
        String endPoiName,
        BigDecimal distance,
        String roadStatus,
        String trafficDensity,
        BigDecimal dangerousLevel,
        BigDecimal tollFee
) {
}
