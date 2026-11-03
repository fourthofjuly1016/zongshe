package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.dto.NeedCreateDTO;
import com.example.demo.entity.Need;
import com.example.demo.service.NeedService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


        /**
     * 验证：需求总数 = 待运 + 运输中 + 运抵
     */
    @GetMapping("/stats")
    public Result<Map<String, Object>> getStats() {
        List<Need> allNeeds = needService.list(); // 调用你之前写好的查询全部的方法
        
        long total = allNeeds.size();
        long pending = allNeeds.stream().filter(n -> "VALID".equals(n.getNeedStatus())).count();
        long inTransit = allNeeds.stream().filter(n -> "ASSIGNED".equals(n.getNeedStatus())).count();
        long arrived = allNeeds.stream().filter(n -> "ARRIVED".equals(n.getNeedStatus())).count();
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("需求总数", total);
        stats.put("待运数量(VALID)", pending);
        stats.put("运输中数量(ASSIGNED)", inTransit);
        stats.put("运抵数量(ARRIVED)", arrived);
        
        // 核心验证公式
        boolean isBalanced = total == (pending + inTransit + arrived);
        stats.put("验证公式(总数=待运+运输中+运抵)", isBalanced ? "✅ 成立" : "❌ 不成立");
        
        return Result.ok(stats);
    }

    
}