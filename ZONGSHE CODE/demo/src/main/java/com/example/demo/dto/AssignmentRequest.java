package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AssignmentRequest(
        @NotNull(message = "需求不能为空") @Positive(message = "需求编号必须大于0") Long taskId,
        @NotNull(message = "车辆不能为空") @Positive(message = "车辆编号必须大于0") Integer carId,
        @Positive(message = "司机编号必须大于0") Integer driverId
) {
}
