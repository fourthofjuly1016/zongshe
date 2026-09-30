package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.dto.NeedCreateDTO;
import com.example.demo.entity.Need;
import com.example.demo.service.NeedService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/need")
@RequiredArgsConstructor
public class NeedController {

    private final NeedService needService;

    /** 1. 增加需求 */
    @PostMapping
    public Result<Need> create(@RequestBody @Valid NeedCreateDTO dto) {
        return Result.ok(needService.create(dto));
    }

    /** 2. 失效需求 */
    @PutMapping("/{id}/invalidate")
    public Result<Void> invalidate(@PathVariable Integer id) {
        needService.invalidate(id);
        return Result.ok();
    }

    /** 查询所有需求 */
    @GetMapping("/list")
    public Result<List<Need>> list() {
        return Result.ok(needService.list());
    }
}