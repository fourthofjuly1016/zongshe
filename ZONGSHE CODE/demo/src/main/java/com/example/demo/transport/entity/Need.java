package com.example.demo.transport.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "need")
public class Need {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "need_id")
    private Integer needId;

    @Column(name = "cargo_name")
    private String cargoName;

    @Column(name = "cargo_quantity")
    private String cargoQuantity;

    @Column(name = "need_status")
    private String needStatus;
}
