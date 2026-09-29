package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AssignmentResponse(
        Long assignmentId,
        Long taskId,
        Integer carId,
        String plateNumber,
        Integer driverId,
        String dispatchStatus,
        BigDecimal estimatedDistance,
        Integer estimatedDuration,
        LocalDateTime assignedAt
) {
}
