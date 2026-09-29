package com.example.demo.dto;

import java.math.BigDecimal;
import java.util.List;

public record MatchingScoreResponse(
        Long taskId,
        Integer carId,
        BigDecimal score,
        boolean eligible,
        List<String> reasons
) {
}
