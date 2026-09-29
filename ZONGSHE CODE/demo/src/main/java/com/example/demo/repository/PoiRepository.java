package com.example.demo.repository;

import com.example.demo.entity.Poi;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PoiRepository extends JpaRepository<Poi, Integer> {
}