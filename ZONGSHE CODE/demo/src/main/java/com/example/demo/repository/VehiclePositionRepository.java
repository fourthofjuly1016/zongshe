package com.example.demo.repository;

import com.example.demo.entity.VehiclePosition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VehiclePositionRepository extends JpaRepository<VehiclePosition, Long> {
    Optional<VehiclePosition> findTopByCar_CarIdOrderByCollectedAtDesc(Integer carId);
}
