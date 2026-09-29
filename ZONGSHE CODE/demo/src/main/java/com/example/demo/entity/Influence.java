package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "influence")
@Data
public class Influence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer influenceId;

    private String description;
}