package com.example.demo.transport.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "vehicle")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicle_id")
    private Integer vehicleId;

    @Column(name = "vehicle_status")
    private String vehicleStatus;

    @Column(name = "vehicle_type")
    private String vehicleType;

    @Column(name = "vehicle_carriage")
    private Integer vehicleCarriage;

    @Column(name = "vehicle_weight")
    private Double vehicleWeight;

    @Column(name = "current_lat")
    private Double currentLat;

    @Column(name = "current_lng")
    private Double currentLng;

    @Column(name = "route_id")
    private Integer routeId;
}
