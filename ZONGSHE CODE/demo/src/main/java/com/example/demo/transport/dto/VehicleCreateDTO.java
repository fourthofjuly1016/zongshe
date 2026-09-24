package com.example.demo.transport.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VehicleCreateDTO {

    private String vehicleStatus;

    private String vehicleType;

    private Integer vehicleCarriage;

    @NotNull(message = "载重不能为空")
    private Double vehicleWeight;

    private Double currentLat;

    private Double currentLng;

    private Integer routeId;
}