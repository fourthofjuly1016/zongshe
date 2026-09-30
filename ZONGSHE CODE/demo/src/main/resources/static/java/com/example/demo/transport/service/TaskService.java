package com.example.demo.transport.service;

import com.example.demo.transport.common.BusinessException;
import com.example.demo.transport.dto.TaskAssignDTO;
import com.example.demo.transport.entity.Driver;
import com.example.demo.transport.entity.Need;
import com.example.demo.transport.entity.TransTask;
import com.example.demo.transport.entity.Vehicle;
import com.example.demo.transport.repository.DriverRepository;
import com.example.demo.transport.repository.NeedRepository;
import com.example.demo.transport.repository.TransTaskRepository;
import com.example.demo.transport.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TransTaskRepository taskRepository;
    private final NeedRepository needRepository;
    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;

    @Transactional
    public TransTask assign(TaskAssignDTO dto) {
        Need need = needRepository.findById(dto.getNeedId())
                .orElseThrow(() -> new BusinessException("需求不存在"));
        Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId())
                .orElseThrow(() -> new BusinessException("车辆不存在"));
        driverRepository.findById(dto.getDriverId())
                .orElseThrow(() -> new BusinessException("司机不存在"));

        if (!"VALID".equals(need.getNeedStatus())) {
            throw new BusinessException("需求不可指派: " + need.getNeedStatus());
        }
        if (!"IDLE".equals(vehicle.getVehicleStatus())) {
            throw new BusinessException("车辆不可指派: " + vehicle.getVehicleStatus());
        }

        TransTask task = new TransTask();
        task.setNeedId(dto.getNeedId());
        task.setVehicleId(dto.getVehicleId());
        task.setDriverId(dto.getDriverId());
        task.setTimeLimit(dto.getTimeLimit());
        task.setTaskStatus("CREATED");
        taskRepository.save(task);

        need.setNeedStatus("ASSIGNED");
        vehicle.setVehicleStatus("BUSY");
        needRepository.save(need);
        vehicleRepository.save(vehicle);

        return task;
    }

    public Double calcRevenue(Integer taskId) {
        TransTask task = taskRepository.findById(taskId)
                .orElseThrow(() -> new BusinessException("任务不存在"));

        double distance = 100.0;
        double revenue = distance * 5.0;

        task.setSalary(revenue);
        taskRepository.save(task);
        return revenue;
    }
}