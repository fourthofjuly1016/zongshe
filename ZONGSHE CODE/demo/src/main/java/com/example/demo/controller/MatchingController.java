package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.dto.MatchingScoreRequest;
import com.example.demo.dto.MatchingScoreResponse;
import com.example.demo.service.DemandService;
import com.example.demo.service.MatchingService;
import com.example.demo.service.CarService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/matching")
public class MatchingController {
    private final MatchingService matchingService;
    private final DemandService demandService;
    private final CarService carService;

    public MatchingController(MatchingService matchingService, DemandService demandService, CarService carService) {
        this.matchingService = matchingService;
        this.demandService = demandService;
        this.carService = carService;
    }

    @PostMapping("/score")
    public ApiResponse<MatchingScoreResponse> score(@Valid @RequestBody MatchingScoreRequest request) {
        return ApiResponse.success("车辆需求匹配度计算成功",
                matchingService.score(demandService.findActiveEntity(request.taskId()),
                        carService.findActiveEntity(request.carId())));
    }
}
