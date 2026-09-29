package com.example.demo.dto;

import java.math.BigDecimal;

public record PoiResponse(
        Integer poiId,
        String name,
        String type,
        String address,
        BigDecimal latitude,
        BigDecimal longitude,
        Integer stayTime
) {
}
