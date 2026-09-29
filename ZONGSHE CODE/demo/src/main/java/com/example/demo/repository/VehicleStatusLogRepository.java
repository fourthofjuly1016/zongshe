package com.example.demo.repository;

import com.example.demo.entity.VehicleStatusLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleStatusLogRepository extends JpaRepository<VehicleStatusLog, Long> {
}
