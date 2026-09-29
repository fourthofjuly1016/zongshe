package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.dto.PoiRequest;
import com.example.demo.dto.PoiResponse;
import com.example.demo.service.PoiService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pois")
@Validated
public class PoiController {
    private final PoiService poiService;

    public PoiController(PoiService poiService) {
        this.poiService = poiService;
    }

    @GetMapping
    public ApiResponse<List<PoiResponse>> list() {
        return ApiResponse.success("查询POI列表成功", poiService.findAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<PoiResponse> get(@PathVariable @Positive Integer id) {
        return ApiResponse.success("查询POI成功", poiService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PoiResponse> create(@Valid @RequestBody PoiRequest request) {
        return ApiResponse.success("新增POI成功", poiService.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<PoiResponse> update(@PathVariable @Positive Integer id,
                                            @Valid @RequestBody PoiRequest request) {
        return ApiResponse.success("修改POI成功", poiService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable @Positive Integer id) {
        poiService.delete(id);
        return ApiResponse.success("删除POI成功");
    }
}
