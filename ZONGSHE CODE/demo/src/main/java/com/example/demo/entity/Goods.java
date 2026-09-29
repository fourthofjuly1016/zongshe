package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "goods")
@Data
public class Goods {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer goodsId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 64)
    private String category;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal goodsWeight;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal goodsVolume;

    @Column(nullable = false)
    private Boolean goodsIsdangerous;

    @Column(nullable = false)
    private Integer goodsPriority;
}
