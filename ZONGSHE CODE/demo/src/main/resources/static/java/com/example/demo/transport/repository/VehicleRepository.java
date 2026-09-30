package com.example.demo.transport.repository;

import com.example.demo.transport.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {
    List<Vehicle> findByVehicleStatus(String vehicleStatus);
}