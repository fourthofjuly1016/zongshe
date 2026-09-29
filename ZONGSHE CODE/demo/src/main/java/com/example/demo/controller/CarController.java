package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.dto.CarRequest;
import com.example.demo.dto.CarResponse;
import com.example.demo.service.CarService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
@Validated
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public ApiResponse<List<CarResponse>> list() {
        return ApiResponse.success("查询车辆列表成功", carService.findAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<CarResponse> get(@PathVariable @Positive Integer id) {
        return ApiResponse.success("查询车辆成功", carService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CarResponse> create(@Valid @RequestBody CarRequest request) {
        return ApiResponse.success("新增车辆成功", carService.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<CarResponse> update(@PathVariable @Positive Integer id,
                                            @Valid @RequestBody CarRequest request) {
        return ApiResponse.success("修改车辆成功", carService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable @Positive Integer id) {
        carService.delete(id);
        return ApiResponse.success("删除车辆成功");
    }
}
