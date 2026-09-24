package com.example.demo.transport.repository;

import com.example.demo.transport.entity.Factory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FactoryRepository extends JpaRepository<Factory, Integer> {
}