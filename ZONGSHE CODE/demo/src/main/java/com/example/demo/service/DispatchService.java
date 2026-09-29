package com.example.demo.service;

import com.example.demo.dto.AssignmentRequest;
import com.example.demo.dto.AssignmentResponse;
import com.example.demo.dto.MatchingScoreResponse;
import com.example.demo.dto.RevenueResponse;
import com.example.demo.entity.Car;
import com.example.demo.entity.DispatchAssignment;
import com.example.demo.entity.Driver;
import com.example.demo.entity.TransportTask;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.DispatchAssignmentRepository;
import com.example.demo.repository.DriverRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class DispatchService {
    private final DispatchAssignmentRepository assignmentRepository;
    private final DemandService demandService;
    private final CarService carService;
    private final DriverRepository driverRepository;
    private final MatchingService matchingService;

    public DispatchService(DispatchAssignmentRepository assignmentRepository,
                           DemandService demandService,
                           CarService carService,
                           DriverRepository driverRepository,
                           MatchingService matchingService) {
        this.assignmentRepository = assignmentRepository;
        this.demandService = demandService;
        this.carService = carService;
        this.driverRepository = driverRepository;
        this.matchingService = matchingService;
    }

    @Transactional
    public AssignmentResponse assign(AssignmentRequest request) {
        TransportTask task = demandService.findActiveEntity(request.taskId());
        Car car = carService.findActiveEntity(request.carId());
        MatchingScoreResponse score = matchingService.score(task, car);
        if (!score.eligible()) {
            throw new IllegalArgumentException("车辆不满足需求匹配条件：" + String.join("；", score.reasons()));
        }
        if (assignmentRepository.existsByTask_TaskIdAndDispatchStatusIn(task.getTaskId(),
                List.of("ASSIGNED", "RUNNING"))) {
            throw new IllegalArgumentException("该需求已经存在有效指派");
        }

        Driver driver = null;
        if (request.driverId() != null) {
            driver = driverRepository.findById(request.driverId())
                    .orElseThrow(() -> new ResourceNotFoundException("司机", request.driverId()));
        }
        DispatchAssignment assignment = new DispatchAssignment();
        assignment.setTask(task);
        assignment.setCar(car);
        assignment.setDriver(driver);
        assignment.setDispatchStatus("ASSIGNED");
        assignment.setAssignedAt(java.time.LocalDateTime.now());
        BigDecimal distance = distance(task);
        assignment.setEstimatedDistance(distance);
        assignment.setEstimatedDuration(distance.multiply(BigDecimal.valueOf(1.5)).intValue());
        task.setTaskStatus("ASSIGNED");
        car.setCarStatus("ASSIGNED");
        return toResponse(assignmentRepository.save(assignment));
    }

    @Transactional(readOnly = true)
    public AssignmentResponse findById(Long id) {
        return toResponse(assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("调度指派", id)));
    }

    @Transactional(readOnly = true)
    public RevenueResponse revenue(Long id, BigDecimal unitPrice, BigDecimal fuelCostPerKm) {
        if (unitPrice.signum() < 0 || fuelCostPerKm.signum() < 0) {
            throw new IllegalArgumentException("单价和每公里油费不能小于0");
        }
        DispatchAssignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("调度指派", id));
        BigDecimal distance = assignment.getEstimatedDistance() == null ? distance(assignment.getTask()) : assignment.getEstimatedDistance();
        BigDecimal gross = distance.multiply(unitPrice).multiply(assignment.getTask().getQuantity());
        BigDecimal fuel = distance.multiply(fuelCostPerKm);
        BigDecimal net = gross.subtract(fuel).setScale(2, RoundingMode.HALF_UP);
        return new RevenueResponse(id, distance, gross.setScale(2, RoundingMode.HALF_UP), BigDecimal.ZERO,
                fuel.setScale(2, RoundingMode.HALF_UP), net,
                "净收益 = 距离 × 运价 × 数量 - 距离 × 每公里油费（当前未绑定道路收费）");
    }

    private BigDecimal distance(TransportTask task) {
        double lat1 = task.getPickupPoi().getLatitude().doubleValue();
        double lon1 = task.getPickupPoi().getLongitude().doubleValue();
        double lat2 = task.getDeliveryPoi().getLatitude().doubleValue();
        double lon2 = task.getDeliveryPoi().getLongitude().doubleValue();
        double earthRadiusKm = 6371.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return BigDecimal.valueOf(earthRadiusKm * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private AssignmentResponse toResponse(DispatchAssignment assignment) {
        return new AssignmentResponse(assignment.getAssignmentId(), assignment.getTask().getTaskId(),
                assignment.getCar().getCarId(), assignment.getCar().getPlateNumber(),
                assignment.getDriver() == null ? null : assignment.getDriver().getDriverId(),
                assignment.getDispatchStatus(), assignment.getEstimatedDistance(), assignment.getEstimatedDuration(),
                assignment.getAssignedAt());
    }
}
