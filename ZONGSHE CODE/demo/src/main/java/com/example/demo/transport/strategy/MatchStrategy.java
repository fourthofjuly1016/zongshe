package com.example.demo.transport.strategy;

import com.example.demo.transport.entity.Need;
import com.example.demo.transport.entity.Vehicle;

public interface MatchStrategy {
    double score(Need need, Vehicle vehicle);
}