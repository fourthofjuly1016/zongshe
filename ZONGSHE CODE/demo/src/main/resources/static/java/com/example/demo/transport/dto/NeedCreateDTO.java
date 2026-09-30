package com.example.demo.transport.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NeedCreateDTO {

    @NotBlank(message = "货物名称不能为空")
    private String cargoName;

    @NotBlank(message = "货物数量不能为空")
    private String cargoQuantity;
}