package com.example.demo.transport.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "cargo")
public class Cargo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cargo_id")
    private Integer cargoId;

    @Column(name = "cargo_weight")
    private Double cargoWeight;

    @Column(name = "cargo_volume")
    private Double cargoVolume;

    @Column(name = "cargo_isdangerous")
    private Boolean cargoIsdangerous;

    @Column(name = "cargo_priority")
    private Integer cargoPriority;

    @Column(name = "cargo_name")
    private String cargoName;
}