package com.example.demo.transport.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LocationDTO {

    @NotNull(message = "纬度不能为空")
    private Double lat;

    @NotNull(message = "经度不能为空")
    private Double lng;
}