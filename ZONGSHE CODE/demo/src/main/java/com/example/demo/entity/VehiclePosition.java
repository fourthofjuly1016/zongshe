package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehicle_position")
@Data
public class VehiclePosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long positionId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @Column(nullable = false, precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(precision = 8, scale = 2)
    private BigDecimal speed;

    @Column(precision = 6, scale = 2)
    private BigDecimal heading;

    @Column(nullable = false)
    private Boolean gpsValid = true;

    @Column(nullable = false, columnDefinition = "datetime")
    private LocalDateTime collectedAt;

    @Column(nullable = false, length = 32)
    private String source = "MODBUS";
}
