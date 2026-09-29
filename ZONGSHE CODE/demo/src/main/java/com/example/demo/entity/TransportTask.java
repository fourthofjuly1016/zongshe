package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transport_task")
@Data
public class TransportTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    @Column(nullable = false, unique = true, length = 40)
    private String taskNo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "goods_id", nullable = false)
    private Goods goods;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pickup_poi_id", nullable = false)
    private Poi pickupPoi;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "delivery_poi_id", nullable = false)
    private Poi deliveryPoi;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity;

    @Column(nullable = false)
    private Integer priority;

    @Column(nullable = false, length = 32)
    private String taskStatus;

    @Column(nullable = false, length = 32)
    private String source;

    @Column(nullable = false)
    private Boolean deleted = false;

    private LocalDateTime plannedPickupTime;
    private LocalDateTime plannedDeliveryTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
