package com.example.demo.dto;

import java.math.BigDecimal;

public record RevenueResponse(
        Long assignmentId,
        BigDecimal distance,
        BigDecimal grossRevenue,
        BigDecimal tollCost,
        BigDecimal fuelCost,
        BigDecimal netRevenue,
        String formula
) {
}
