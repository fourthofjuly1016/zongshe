package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "abnorm_incident")
@Data
public class AbnormIncident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer incidentId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Double dangerousLevel;

    // 关联车辆（多对一）
    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    // 关联道路（多对一）
    @ManyToOne
    @JoinColumn(name = "road_id")
    private Road road;
}