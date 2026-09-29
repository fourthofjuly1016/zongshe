package com.example.demo.dto;

import java.math.BigDecimal;

public record GoodsResponse(
        Integer goodsId,
        String name,
        String category,
        BigDecimal goodsWeight,
        BigDecimal goodsVolume,
        Boolean goodsIsdangerous,
        Integer goodsPriority
) {
}
