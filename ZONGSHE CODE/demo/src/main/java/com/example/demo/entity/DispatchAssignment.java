package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "dispatch_assignment")
@Data
public class DispatchAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assignmentId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_id", nullable = false)
    private TransportTask task;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @Column(nullable = false, length = 32)
    private String dispatchStatus;

    @Column(nullable = false)
    private LocalDateTime assignedAt;

    @Column(precision = 10, scale = 2)
    private BigDecimal estimatedDistance;

    private Integer estimatedDuration;
}
