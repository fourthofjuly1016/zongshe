package com.example.demo.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record GoodsRequest(
        @NotBlank(message = "货物名称不能为空")
        @Size(max = 100, message = "货物名称不能超过100个字符")
        String name,
        @NotBlank(message = "货物类别不能为空")
        @Size(max = 64, message = "货物类别不能超过64个字符")
        String category,
        @NotNull(message = "货物重量不能为空")
        @DecimalMin(value = "0.01", message = "货物重量必须大于0")
        BigDecimal goodsWeight,
        @NotNull(message = "货物体积不能为空")
        @DecimalMin(value = "0.01", message = "货物体积必须大于0")
        BigDecimal goodsVolume,
        @NotNull(message = "危险品标识不能为空")
        Boolean goodsIsdangerous,
        @NotNull(message = "优先级不能为空")
        @Min(value = 1, message = "优先级不能小于1")
        @Max(value = 5, message = "优先级不能大于5")
        Integer goodsPriority
) {
}
