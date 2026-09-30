package com.example.demo.transport.service;

import com.example.demo.transport.dto.DriverCreateDTO;
import com.example.demo.transport.entity.Driver;
import com.example.demo.transport.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;

    public Driver create(DriverCreateDTO dto) {
        Driver d = new Driver();
        d.setDriverAge(dto.getDriverAge());
        d.setDriverPrefer(dto.getDriverPrefer());
        d.setDriverFamiliar(dto.getDriverFamiliar());
        return driverRepository.save(d);
    }
}