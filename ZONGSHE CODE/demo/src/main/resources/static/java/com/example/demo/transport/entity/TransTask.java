package com.example.demo.transport.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "trans_task")
public class TransTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Integer taskId;

    @Column(name = "time_limit")
    private LocalDateTime timeLimit;

    @Column(name = "salary")
    private Double salary;

    @Column(name = "vehicle_id")
    private Integer vehicleId;

    @Column(name = "need_id")
    private Integer needId;

    @Column(name = "driver_id")
    private Integer driverId;

    @Column(name = "task_status")
    private String taskStatus;
}
