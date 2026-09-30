package com.example.demo.transport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchScoreVO {
    private Integer vehicleId;
    private Double score;
}