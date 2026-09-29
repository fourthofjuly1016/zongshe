package com.example.demo.service;

import com.example.demo.dto.PoiRequest;
import com.example.demo.dto.PoiResponse;
import com.example.demo.entity.Poi;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.PoiRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PoiService {
    private final PoiRepository poiRepository;

    public PoiService(PoiRepository poiRepository) {
        this.poiRepository = poiRepository;
    }

    public List<PoiResponse> findAll() {
        return poiRepository.findAll(Sort.by("poiId")).stream().map(this::toResponse).toList();
    }

    public PoiResponse findById(Integer id) {
        return toResponse(findEntity(id));
    }

    @Transactional
    public PoiResponse create(PoiRequest request) {
        return toResponse(poiRepository.save(apply(new Poi(), request)));
    }

    @Transactional
    public PoiResponse update(Integer id, PoiRequest request) {
        return toResponse(poiRepository.save(apply(findEntity(id), request)));
    }

    @Transactional
    public void delete(Integer id) {
        poiRepository.delete(findEntity(id));
    }

    Poi findEntity(Integer id) {
        return poiRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("POI", id));
    }

    private Poi apply(Poi poi, PoiRequest request) {
        poi.setName(request.name());
        poi.setType(request.type());
        poi.setAddress(request.address());
        poi.setLatitude(request.latitude());
        poi.setLongitude(request.longitude());
        poi.setStayTime(request.stayTime());
        return poi;
    }

    private PoiResponse toResponse(Poi poi) {
        return new PoiResponse(poi.getPoiId(), poi.getName(), poi.getType(), poi.getAddress(),
                poi.getLatitude(), poi.getLongitude(), poi.getStayTime());
    }
}
