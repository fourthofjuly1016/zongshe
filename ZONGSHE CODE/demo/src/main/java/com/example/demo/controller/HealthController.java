package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/api/health")
    public ApiResponse<Map<String, Object>> health() {
        return ApiResponse.success("服务运行正常", Map.of(
                "service", "transport-dispatch-backend",
                "status", "UP",
                "time", OffsetDateTime.now().toString()
        ));
    }
}
