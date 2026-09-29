package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.dto.DemandRequest;
import com.example.demo.dto.DemandResponse;
import com.example.demo.service.DemandService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/demands")
@Validated
public class DemandController {
    private final DemandService demandService;

    public DemandController(DemandService demandService) {
        this.demandService = demandService;
    }

    @GetMapping
    public ApiResponse<List<DemandResponse>> list() {
        return ApiResponse.success("查询需求列表成功", demandService.findAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<DemandResponse> get(@PathVariable @Positive Long id) {
        return ApiResponse.success("查询需求成功", demandService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<DemandResponse> create(@Valid @RequestBody DemandRequest request) {
        return ApiResponse.success("工厂需求生成成功", demandService.create(request));
    }

    @PatchMapping("/{id}/invalidate")
    public ApiResponse<Void> invalidate(@PathVariable @Positive Long id) {
        demandService.invalidate(id);
        return ApiResponse.success("需求已失效");
    }
}
