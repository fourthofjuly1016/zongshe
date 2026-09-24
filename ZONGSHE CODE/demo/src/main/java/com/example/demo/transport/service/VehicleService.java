package com.example.demo.transport.service;

import com.example.demo.transport.common.BusinessException;
import com.example.demo.transport.dto.LocationDTO;
import com.example.demo.transport.dto.VehicleCreateDTO;
import com.example.demo.transport.entity.Vehicle;
import com.example.demo.transport.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public Vehicle create(VehicleCreateDTO dto) {
        Vehicle v = new Vehicle();
        v.setVehicleStatus(dto.getVehicleStatus() != null ? dto.getVehicleStatus() : "IDLE");
        v.setVehicleType(dto.getVehicleType());
        v.setVehicleCarriage(dto.getVehicleCarriage());
        v.setVehicleWeight(dto.getVehicleWeight());
        v.setCurrentLat(dto.getCurrentLat());
        v.setCurrentLng(dto.getCurrentLng());
        v.setRouteId(dto.getRouteId());
        return vehicleRepository.save(v);
    }

    public void invalidate(Integer id) {
        Vehicle v = vehicleRepository.findById(id)
                .orElseThrow(() -> new BusinessException("车辆不存在"));
        v.setVehicleStatus("INVALID");
        vehicleRepository.save(v);
    }

    public void updateLocation(Integer id, LocationDTO dto) {
        Vehicle v = vehicleRepository.findById(id)
                .orElseThrow(() -> new BusinessException("车辆不存在"));
        v.setCurrentLat(dto.getLat());
        v.setCurrentLng(dto.getLng());
        vehicleRepository.save(v);
    }

    public void updateStatus(Integer id, String status) {
        Vehicle v = vehicleRepository.findById(id)
                .orElseThrow(() -> new BusinessException("车辆不存在"));
        v.setVehicleStatus(status);
        vehicleRepository.save(v);
    }
}