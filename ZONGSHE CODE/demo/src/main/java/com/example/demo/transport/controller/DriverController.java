package com.example.demo.transport.controller;

import com.example.demo.transport.common.Result;
import com.example.demo.transport.dto.DriverCreateDTO;
import com.example.demo.transport.entity.Driver;
import com.example.demo.transport.service.DriverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/driver")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    /** 增加司机 */
    @PostMapping
    public Result<Driver> create(@RequestBody @Valid DriverCreateDTO dto) {
        return Result.ok(driverService.create(dto));
    }
}