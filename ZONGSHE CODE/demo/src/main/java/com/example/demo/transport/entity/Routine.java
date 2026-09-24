package com.example.demo.transport.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "routine")
public class Routine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "routine_id")
    private Integer routineId;

    @Column(name = "start_poi")
    private Integer startPoi;

    @Column(name = "end_poi")
    private Integer endPoi;

    @Column(name = "distance")
    private Double distance;

    @Column(name = "routine_status")
    private String routineStatus;

    @Column(name = "traffic_density")
    private String trafficDensity;

    @Column(name = "dangerous_level")
    private Double dangerousLevel;

    @Column(name = "routine_name")
    private String routineName;
}