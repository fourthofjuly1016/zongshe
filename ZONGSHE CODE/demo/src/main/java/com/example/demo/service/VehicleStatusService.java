package com.example.demo.service;

import com.example.demo.dto.CarResponse;
import com.example.demo.dto.VehicleStatusRequest;
import com.example.demo.entity.Car;
import com.example.demo.entity.TransportTask;
import com.example.demo.entity.VehicleStatusLog;
import com.example.demo.repository.CarRepository;
import com.example.demo.repository.TransportTaskRepository;
import com.example.demo.repository.VehicleStatusLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

@Service
public class VehicleStatusService {
    private static final Set<String> ALLOWED = Set.of(
            "IDLE", "TO_PICKUP", "LOADING", "TRANSPORTING", "UNLOADING", "EXCEPTION", "COMPLETED", "ASSIGNED");

    private final CarService carService;
    private final CarRepository carRepository;
    private final TransportTaskRepository taskRepository;
    private final VehicleStatusLogRepository logRepository;

    public VehicleStatusService(CarService carService, CarRepository carRepository,
                                TransportTaskRepository taskRepository,
                                VehicleStatusLogRepository logRepository) {
        this.carService = carService;
        this.carRepository = carRepository;
        this.taskRepository = taskRepository;
        this.logRepository = logRepository;
    }

    @Transactional
    public CarResponse update(Integer carId, VehicleStatusRequest request) {
        String status = request.status().trim().toUpperCase();
        if (!ALLOWED.contains(status)) {
            throw new IllegalArgumentException("不支持的车辆状态：" + status);
        }
        Car car = carService.findActiveEntity(carId);
        TransportTask task = null;
        if (request.taskId() != null) {
            task = taskRepository.findByTaskIdAndDeletedFalse(request.taskId())
                    .orElseThrow(() -> new com.example.demo.exception.ResourceNotFoundException("需求", request.taskId()));
        }
        car.setCarStatus(status);
        carRepository.save(car);
        VehicleStatusLog log = new VehicleStatusLog();
        log.setCar(car);
        log.setTask(task);
        log.setCarStatus(status);
        log.setChangedAt(LocalDateTime.now());
        log.setRemark(request.remark());
        logRepository.save(log);
        return new CarResponse(car.getCarId(), car.getPlateNumber(), car.getCarStatus(), car.getCarType(),
                car.getCarStorage(), car.getMaxWeight(), car.getMaxVolume(), car.getDeleted());
    }
}
