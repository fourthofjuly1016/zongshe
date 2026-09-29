package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.dto.RoadRequest;
import com.example.demo.dto.RoadResponse;
import com.example.demo.service.RoadService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roads")
@Validated
public class RoadController {
    private final RoadService roadService;

    public RoadController(RoadService roadService) {
        this.roadService = roadService;
    }

    @GetMapping
    public ApiResponse<List<RoadResponse>> list() {
        return ApiResponse.success("查询道路列表成功", roadService.findAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<RoadResponse> get(@PathVariable @Positive Integer id) {
        return ApiResponse.success("查询道路成功", roadService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<RoadResponse> create(@Valid @RequestBody RoadRequest request) {
        return ApiResponse.success("新增道路成功", roadService.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<RoadResponse> update(@PathVariable @Positive Integer id,
                                             @Valid @RequestBody RoadRequest request) {
        return ApiResponse.success("修改道路成功", roadService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable @Positive Integer id) {
        roadService.delete(id);
        return ApiResponse.success("删除道路成功");
    }
}
