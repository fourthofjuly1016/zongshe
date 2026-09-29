package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MatchingScoreRequest(
        @NotNull(message = "需求不能为空") @Positive Long taskId,
        @NotNull(message = "车辆不能为空") @Positive Integer carId
) {
}
