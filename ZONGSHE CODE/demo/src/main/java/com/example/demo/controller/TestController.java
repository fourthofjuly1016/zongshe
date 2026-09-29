package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/api/hello")
    public ApiResponse<String> hello() {
        return ApiResponse.success("请求成功", "Hello from Spring Boot!");
    }
}
