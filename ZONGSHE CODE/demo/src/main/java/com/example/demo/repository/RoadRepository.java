package com.example.demo.repository;

import com.example.demo.entity.Road;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoadRepository extends JpaRepository<Road, Integer> {
}