package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "road")
@Data
public class Road {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roadId;

    @Column(nullable = false, length = 100)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "start_poi_id", nullable = false)
    private Poi startPoi;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "end_poi_id", nullable = false)
    private Poi endPoi;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal distance;

    @Column(nullable = false, length = 32)
    private String roadStatus;

    @Column(nullable = false, length = 32)
    private String trafficDensity;

    @Column(nullable = false, precision = 4, scale = 2)
    private BigDecimal dangerousLevel;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal tollFee;
}
