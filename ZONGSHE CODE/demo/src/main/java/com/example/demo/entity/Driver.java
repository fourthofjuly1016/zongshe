package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "driver")
@Data
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer driverId;

    private Integer driverAge;

    private String driverPrefer;

    private String driverFamiliar;
}