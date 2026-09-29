package com.example.demo.service;

import com.example.demo.dto.RoadRequest;
import com.example.demo.dto.RoadResponse;
import com.example.demo.entity.Road;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.RoadRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RoadService {
    private final RoadRepository roadRepository;
    private final PoiService poiService;

    public RoadService(RoadRepository roadRepository, PoiService poiService) {
        this.roadRepository = roadRepository;
        this.poiService = poiService;
    }

    public List<RoadResponse> findAll() {
        return roadRepository.findAll(Sort.by("roadId")).stream().map(this::toResponse).toList();
    }

    public RoadResponse findById(Integer id) {
        return toResponse(findEntity(id));
    }

    @Transactional
    public RoadResponse create(RoadRequest request) {
        return toResponse(roadRepository.save(apply(new Road(), request)));
    }

    @Transactional
    public RoadResponse update(Integer id, RoadRequest request) {
        return toResponse(roadRepository.save(apply(findEntity(id), request)));
    }

    @Transactional
    public void delete(Integer id) {
        roadRepository.delete(findEntity(id));
    }

    private Road findEntity(Integer id) {
        return roadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("道路", id));
    }

    private Road apply(Road road, RoadRequest request) {
        road.setName(request.name());
        road.setStartPoi(poiService.findEntity(request.startPoiId()));
        road.setEndPoi(poiService.findEntity(request.endPoiId()));
        road.setDistance(request.distance());
        road.setRoadStatus(request.roadStatus());
        road.setTrafficDensity(request.trafficDensity());
        road.setDangerousLevel(request.dangerousLevel());
        road.setTollFee(request.tollFee());
        return road;
    }

    private RoadResponse toResponse(Road road) {
        return new RoadResponse(road.getRoadId(), road.getName(),
                road.getStartPoi().getPoiId(), road.getStartPoi().getName(),
                road.getEndPoi().getPoiId(), road.getEndPoi().getName(),
                road.getDistance(), road.getRoadStatus(), road.getTrafficDensity(),
                road.getDangerousLevel(), road.getTollFee());
    }
}
