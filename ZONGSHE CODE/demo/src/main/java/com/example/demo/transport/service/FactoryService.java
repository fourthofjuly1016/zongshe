package com.example.demo.transport.service;

import com.example.demo.transport.entity.Factory;
import com.example.demo.transport.repository.FactoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FactoryService {

    private final FactoryRepository factoryRepository;

    public Factory create(Factory f) {
        return factoryRepository.save(f);
    }
}