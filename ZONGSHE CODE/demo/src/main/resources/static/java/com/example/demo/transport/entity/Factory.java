package com.example.demo.transport.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "factory")
public class Factory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "factory_id")
    private Integer factoryId;

    @Column(name = "factory_name")
    private String factoryName;

    @Column(name = "factory_address")
    private String factoryAddress;
}
