package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.dto.AssignmentRequest;
import com.example.demo.dto.AssignmentResponse;
import com.example.demo.dto.RevenueResponse;
import com.example.demo.service.DispatchService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/dispatch")
@Validated
public class DispatchController {
    private final DispatchService dispatchService;

    public DispatchController(DispatchService dispatchService) {
        this.dispatchService = dispatchService;
    }

    @PostMapping("/assignments")
    public ApiResponse<AssignmentResponse> assign(@Valid @RequestBody AssignmentRequest request) {
        return ApiResponse.success("需求已生成调度指派", dispatchService.assign(request));
    }

    @GetMapping("/assignments/{id}")
    public ApiResponse<AssignmentResponse> get(@PathVariable @Positive Long id) {
        return ApiResponse.success("查询调度指派成功", dispatchService.findById(id));
    }

    @GetMapping("/assignments/{id}/revenue")
    public ApiResponse<RevenueResponse> revenue(
            @PathVariable @Positive Long id,
            @RequestParam(defaultValue = "8") @DecimalMin("0") BigDecimal unitPrice,
            @RequestParam(defaultValue = "1.2") @DecimalMin("0") BigDecimal fuelCostPerKm) {
        return ApiResponse.success("运输收益计算成功", dispatchService.revenue(id, unitPrice, fuelCostPerKm));
    }
}
