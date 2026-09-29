package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "car")
@Data
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer carId;

    @Column(nullable = false, unique = true, length = 32)
    private String plateNumber;

    @Column(nullable = false, length = 32)
    private String carStatus;

    @Column(nullable = false, length = 64)
    private String carType;

    @Column(nullable = false)
    private Integer carStorage;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal maxWeight;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal maxVolume;

    @Column(nullable = false)
    private Boolean deleted = false;
}
