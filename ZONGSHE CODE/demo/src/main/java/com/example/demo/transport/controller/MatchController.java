package com.example.demo.transport.controller;

import com.example.demo.transport.common.Result;
import com.example.demo.transport.dto.MatchScoreVO;
import com.example.demo.transport.service.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/match")
@RequiredArgsConstructor
public class MatchController {

    private final MatchingService matchingService;

    /** 9. 计算匹配度 */
    @GetMapping("/score")
    public Result<MatchScoreVO> score(@RequestParam Integer needId,
                                      @RequestParam Integer vehicleId) {
        return Result.ok(matchingService.score(needId, vehicleId));
    }

    /** 9.1 为需求推荐 Top N 车辆 */
    @GetMapping("/recommend")
    public Result<List<MatchScoreVO>> recommend(@RequestParam Integer needId,
                                                @RequestParam(defaultValue = "5") int topN) {
        return Result.ok(matchingService.recommend(needId, topN));
    }
}