package com.example.demo.transport.repository;

import com.example.demo.transport.entity.AbnormalIncident;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbnormalIncidentRepository extends JpaRepository<AbnormalIncident, Integer> {
}