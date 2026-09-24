package com.example.demo.transport.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "driver")
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "driver_id")
    private Integer driverId;

    @Column(name = "driver_age")
    private Integer driverAge;

    @Column(name = "driver_prefer")
    private String driverPrefer;

    @Column(name = "driver_familiar")
    private String driverFamiliar;
}