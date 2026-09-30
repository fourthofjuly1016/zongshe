package com.example.demo.transport.service;

import com.example.demo.transport.common.BusinessException;
import com.example.demo.transport.dto.MatchScoreVO;
import com.example.demo.transport.entity.Need;
import com.example.demo.transport.entity.Vehicle;
import com.example.demo.transport.repository.NeedRepository;
import com.example.demo.transport.repository.VehicleRepository;
import com.example.demo.transport.strategy.MatchStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchingService {

    private final NeedRepository needRepository;
    private final VehicleRepository vehicleRepository;
    private final MatchStrategy matchStrategy;

    public MatchScoreVO score(Integer needId, Integer vehicleId) {
        Need n = needRepository.findById(needId)
                .orElseThrow(() -> new BusinessException("需求不存在"));
        Vehicle v = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new BusinessException("车辆不存在"));
        return new MatchScoreVO(vehicleId, matchStrategy.score(n, v));
    }

    public List<MatchScoreVO> recommend(Integer needId, int topN) {
        Need n = needRepository.findById(needId)
                .orElseThrow(() -> new BusinessException("需求不存在"));
        List<Vehicle> vehicles = vehicleRepository.findByVehicleStatus("IDLE");

        return vehicles.stream()
                .map(v -> new MatchScoreVO(v.getVehicleId(), matchStrategy.score(n, v)))
                .filter(vo -> vo.getScore() > 0)
                .sorted(Comparator.comparingDouble(MatchScoreVO::getScore).reversed())
                .limit(topN)
                .toList();
    }
}