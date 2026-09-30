package com.example.demo.service;

import com.example.demo.entity.Poi;
import com.example.demo.repository.PoiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PoiService {

    private final PoiRepository poiRepository;

    public List<Poi> list() {
        return poiRepository.findAll();
    }
}