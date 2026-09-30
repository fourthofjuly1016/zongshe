package com.example.demo.repository;

import com.example.demo.entity.Factory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FactoryRepository extends JpaRepository<Factory, Integer> {
}