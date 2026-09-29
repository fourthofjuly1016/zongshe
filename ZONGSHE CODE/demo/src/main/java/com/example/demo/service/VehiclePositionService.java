package com.example.demo.service;

import com.example.demo.dto.VehiclePositionRequest;
import com.example.demo.dto.VehiclePositionResponse;
import com.example.demo.entity.Car;
import com.example.demo.entity.VehiclePosition;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.VehiclePositionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class VehiclePositionService {
    private final VehiclePositionRepository positionRepository;
    private final CarService carService;

    public VehiclePositionService(VehiclePositionRepository positionRepository, CarService carService) {
        this.positionRepository = positionRepository;
        this.carService = carService;
    }

    @Transactional
    public VehiclePositionResponse update(Integer carId, VehiclePositionRequest request) {
        Car car = carService.findActiveEntity(carId);
        VehiclePosition position = new VehiclePosition();
        position.setCar(car);
        position.setLatitude(request.latitude());
        position.setLongitude(request.longitude());
        position.setSpeed(request.speed());
        position.setHeading(request.heading());
        position.setGpsValid(request.gpsValid() == null || request.gpsValid());
        position.setCollectedAt(request.collectedAt() == null ? LocalDateTime.now() : request.collectedAt());
        position.setSource(request.source() == null || request.source().isBlank() ? "API" : request.source());
        return toResponse(positionRepository.save(position));
    }

    public VehiclePositionResponse latest(Integer carId) {
        carService.findActiveEntity(carId);
        VehiclePosition position = positionRepository.findTopByCar_CarIdOrderByCollectedAtDesc(carId)
                .orElseThrow(() -> new ResourceNotFoundException("车辆位置", carId));
        return toResponse(position);
    }

    private VehiclePositionResponse toResponse(VehiclePosition position) {
        return new VehiclePositionResponse(position.getPositionId(), position.getCar().getCarId(),
                position.getCar().getPlateNumber(), position.getLatitude(), position.getLongitude(),
                position.getSpeed(), position.getHeading(), position.getGpsValid(),
                position.getCollectedAt(), position.getSource());
    }
}
