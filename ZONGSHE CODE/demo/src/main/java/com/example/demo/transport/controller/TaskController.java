package com.example.demo.transport.controller;

import com.example.demo.transport.common.Result;
import com.example.demo.transport.dto.TaskAssignDTO;
import com.example.demo.transport.entity.TransTask;
import com.example.demo.transport.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    /** 5. 生成并指派任务 */
    @PostMapping("/assign")
    public Result<TransTask> assign(@RequestBody @Valid TaskAssignDTO dto) {
        return Result.ok(taskService.assign(dto));
    }

    /** 8. 计算运输收益 */
    @GetMapping("/{taskId}/revenue")
    public Result<Double> calcRevenue(@PathVariable Integer taskId) {
        return Result.ok(taskService.calcRevenue(taskId));
    }
}