package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "poi")
@Data
public class Poi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer poiId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 32)
    private String type;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(nullable = false)
    private Integer stayTime;
}
