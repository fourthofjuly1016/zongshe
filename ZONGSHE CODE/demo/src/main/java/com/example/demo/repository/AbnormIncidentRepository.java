package com.example.demo.repository;

import com.example.demo.entity.AbnormIncident;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbnormIncidentRepository extends JpaRepository<AbnormIncident, Integer> {
}