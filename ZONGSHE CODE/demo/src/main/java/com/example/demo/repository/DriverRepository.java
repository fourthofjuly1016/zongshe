package com.example.demo.repository;

import com.example.demo.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Integer> {
}