package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "drive")
@Data
public class Drive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer driveId;

    @ManyToOne
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}