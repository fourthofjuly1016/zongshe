package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.dto.GoodsRequest;
import com.example.demo.dto.GoodsResponse;
import com.example.demo.service.GoodsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goods")
@Validated
public class GoodsController {
    private final GoodsService goodsService;

    public GoodsController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    @GetMapping
    public ApiResponse<List<GoodsResponse>> list() {
        return ApiResponse.success("查询货物列表成功", goodsService.findAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<GoodsResponse> get(@PathVariable @Positive Integer id) {
        return ApiResponse.success("查询货物成功", goodsService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<GoodsResponse> create(@Valid @RequestBody GoodsRequest request) {
        return ApiResponse.success("新增货物成功", goodsService.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<GoodsResponse> update(@PathVariable @Positive Integer id,
                                              @Valid @RequestBody GoodsRequest request) {
        return ApiResponse.success("修改货物成功", goodsService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable @Positive Integer id) {
        goodsService.delete(id);
        return ApiResponse.success("删除货物成功");
    }
}
