package com.example.demo.transport.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "poi")
public class Poi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "poi_id")
    private Integer poiId;

    @Column(name = "poi_name")
    private String poiName;

    @Column(name = "poi_type")
    private String poiType;

    @Column(name = "poi_address")
    private String poiAddress;

    @Column(name = "stay_period")
    private LocalDateTime stayPeriod;
}