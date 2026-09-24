package com.example.demo.transport.repository;

import com.example.demo.transport.entity.Poi;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PoiRepository extends JpaRepository<Poi, Integer> {
}