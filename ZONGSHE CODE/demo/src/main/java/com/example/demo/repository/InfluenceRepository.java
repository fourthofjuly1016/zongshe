package com.example.demo.repository;

import com.example.demo.entity.Influence;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InfluenceRepository extends JpaRepository<Influence, Integer> {
}