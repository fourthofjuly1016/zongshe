package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "vehicle_status_log")
@Data
public class VehicleStatusLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long statusLogId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private TransportTask task;

    @Column(nullable = false, length = 32)
    private String carStatus;

    @Column(nullable = false)
    private LocalDateTime changedAt;

    private String remark;
}
