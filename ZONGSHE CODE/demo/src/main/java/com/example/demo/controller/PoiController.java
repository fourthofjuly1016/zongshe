package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.entity.Poi;
import com.example.demo.service.PoiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/poi")
@RequiredArgsConstructor
public class PoiController {

    private final PoiService poiService;

    /** 查询所有点位 */
    @GetMapping("/list")
    public Result<List<Poi>> list() {
        return Result.ok(poiService.list());
    }
}