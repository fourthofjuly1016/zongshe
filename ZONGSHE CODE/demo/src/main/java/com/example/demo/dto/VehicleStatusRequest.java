package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record VehicleStatusRequest(
        @NotBlank(message = "车辆状态不能为空") @Size(max = 32) String status,
        @Positive(message = "任务编号必须大于0") Long taskId,
        @Size(max = 255, message = "备注不能超过255个字符") String remark
) {
}
