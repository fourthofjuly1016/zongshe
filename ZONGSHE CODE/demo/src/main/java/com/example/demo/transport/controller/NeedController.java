package com.example.demo.transport.controller;

import com.example.demo.transport.common.Result;
import com.example.demo.transport.dto.NeedCreateDTO;
import com.example.demo.transport.entity.Need;
import com.example.demo.transport.service.NeedService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    /** 2. 失效需求（软删除） */
    @PutMapping("/{id}/invalidate")
    public Result<Void> invalidate(@PathVariable Integer id) {
        needService.invalidate(id);
        return Result.ok();
    }
}