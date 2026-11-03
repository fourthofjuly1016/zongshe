package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.entity.Factory;
import com.example.demo.repository.FactoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/factory")
@RequiredArgsConstructor
public class FactoryController {

    private final FactoryRepository factoryRepository;

    @GetMapping("/list")
    public Result<List<Factory>> list() {
        return Result.ok(factoryRepository.findAll());
    }
}