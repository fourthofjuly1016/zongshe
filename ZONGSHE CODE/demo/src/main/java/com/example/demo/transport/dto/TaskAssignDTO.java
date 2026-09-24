package com.example.demo.transport.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskAssignDTO {

    @NotNull(message = "需求ID不能为空")
    private Integer needId;

    @NotNull(message = "车辆ID不能为空")
    private Integer vehicleId;

    @NotNull(message = "司机ID不能为空")
    private Integer driverId;

    private LocalDateTime timeLimit;
}
