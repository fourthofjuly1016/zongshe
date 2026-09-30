package com.example.demo.strategy;

import com.example.demo.entity.Need;
import com.example.demo.entity.Vehicle;

public interface MatchStrategy {
    double score(Need need, Vehicle vehicle);
}