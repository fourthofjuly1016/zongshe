package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.dto.LocationDTO;
import com.example.demo.dto.VehicleCreateDTO;
import com.example.demo.entity.Vehicle;
import com.example.demo.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicle")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    /** 3. 增加车辆 */
    @PostMapping
    public Result<Vehicle> create(@RequestBody @Valid VehicleCreateDTO dto) {
        return Result.ok(vehicleService.create(dto));
    }

    /** 4. 失效车辆 */
    @PutMapping("/{id}/invalidate")
    public Result<Void> invalidate(@PathVariable Integer id) {
        vehicleService.invalidate(id);
        return Result.ok();
    }

    /** 6. 更新车辆位置 */
    @PutMapping("/{id}/location")
    public Result<Void> updateLocation(@PathVariable Integer id,
                                       @RequestBody @Valid LocationDTO dto) {
        vehicleService.updateLocation(id, dto);
        return Result.ok();
    }

    /** 7. 更新车辆状态 */
    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Integer id,
                                     @RequestParam String status) {
        vehicleService.updateStatus(id, status);
        return Result.ok();
    }

    /** 查询所有车辆 */
    @GetMapping("/list")
    public Result<List<Vehicle>> list() {
        return Result.ok(vehicleService.list());
    }
}