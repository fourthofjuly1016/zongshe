package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.dto.CarRequest;
import com.example.demo.dto.CarResponse;
import com.example.demo.dto.VehiclePositionRequest;
import com.example.demo.dto.VehiclePositionResponse;
import com.example.demo.dto.VehicleStatusRequest;
import com.example.demo.service.CarService;
import com.example.demo.service.VehiclePositionService;
import com.example.demo.service.VehicleStatusService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vehicles")
@Validated
public class VehicleController {
    private final CarService carService;
    private final VehiclePositionService positionService;
    private final VehicleStatusService statusService;

    public VehicleController(CarService carService, VehiclePositionService positionService,
                             VehicleStatusService statusService) {
        this.carService = carService;
        this.positionService = positionService;
        this.statusService = statusService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CarResponse> create(@Valid @RequestBody CarRequest request) {
        return ApiResponse.success("车辆生成成功", carService.create(request));
    }

    @PatchMapping("/{id}/invalidate")
    public ApiResponse<Void> invalidate(@PathVariable @Positive Integer id) {
        carService.delete(id);
        return ApiResponse.success("车辆已失效");
    }

    @PostMapping("/{id}/position")
    public ApiResponse<VehiclePositionResponse> updatePosition(@PathVariable @Positive Integer id,
                                                                @Valid @RequestBody VehiclePositionRequest request) {
        return ApiResponse.success("车辆位置已更新", positionService.update(id, request));
    }

    @GetMapping("/{id}/position/latest")
    public ApiResponse<VehiclePositionResponse> latestPosition(@PathVariable @Positive Integer id) {
        return ApiResponse.success("查询车辆最新位置成功", positionService.latest(id));
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<CarResponse> updateStatus(@PathVariable @Positive Integer id,
                                                  @Valid @RequestBody VehicleStatusRequest request) {
        return ApiResponse.success("车辆状态已更新", statusService.update(id, request));
    }
}
