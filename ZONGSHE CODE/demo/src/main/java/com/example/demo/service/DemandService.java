package com.example.demo.service;

import com.example.demo.dto.DemandRequest;
import com.example.demo.dto.DemandResponse;
import com.example.demo.entity.TransportTask;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.GoodsRepository;
import com.example.demo.repository.PoiRepository;
import com.example.demo.repository.TransportTaskRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class DemandService {
    private static final DateTimeFormatter TASK_TIME = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final TransportTaskRepository taskRepository;
    private final GoodsRepository goodsRepository;
    private final PoiRepository poiRepository;

    public DemandService(TransportTaskRepository taskRepository,
                         GoodsRepository goodsRepository,
                         PoiRepository poiRepository) {
        this.taskRepository = taskRepository;
        this.goodsRepository = goodsRepository;
        this.poiRepository = poiRepository;
    }

    public List<DemandResponse> findAll() {
        return taskRepository.findAllByDeletedFalse(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream().map(this::toResponse).toList();
    }

    public DemandResponse findById(Long id) {
        return toResponse(findActiveEntity(id));
    }

    @Transactional
    public DemandResponse create(DemandRequest request) {
        if (request.pickupPoiId().equals(request.deliveryPoiId())) {
            throw new IllegalArgumentException("装货点和卸货点不能相同");
        }
        TransportTask task = new TransportTask();
        task.setTaskNo("FACTORY-" + LocalDateTime.now().format(TASK_TIME) + "-" + System.nanoTime() % 10000);
        task.setGoods(goodsRepository.findById(request.goodsId())
                .orElseThrow(() -> new ResourceNotFoundException("货物", request.goodsId())));
        task.setPickupPoi(poiRepository.findById(request.pickupPoiId())
                .orElseThrow(() -> new ResourceNotFoundException("装货点POI", request.pickupPoiId())));
        task.setDeliveryPoi(poiRepository.findById(request.deliveryPoiId())
                .orElseThrow(() -> new ResourceNotFoundException("卸货点POI", request.deliveryPoiId())));
        task.setQuantity(request.quantity());
        task.setPriority(request.priority());
        task.setTaskStatus("PENDING");
        task.setSource("FACTORY");
        task.setDeleted(false);
        task.setPlannedPickupTime(request.plannedPickupTime());
        task.setPlannedDeliveryTime(request.plannedDeliveryTime());
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        return toResponse(taskRepository.save(task));
    }

    @Transactional
    public void invalidate(Long id) {
        TransportTask task = findActiveEntity(id);
        task.setDeleted(true);
        task.setTaskStatus("INVALID");
        task.setUpdatedAt(LocalDateTime.now());
        taskRepository.save(task);
    }

    public TransportTask findActiveEntity(Long id) {
        return taskRepository.findByTaskIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("需求", id));
    }

    private DemandResponse toResponse(TransportTask task) {
        return new DemandResponse(task.getTaskId(), task.getTaskNo(),
                task.getGoods().getGoodsId(), task.getGoods().getName(),
                task.getPickupPoi().getPoiId(), task.getPickupPoi().getName(),
                task.getDeliveryPoi().getPoiId(), task.getDeliveryPoi().getName(),
                task.getQuantity(), task.getPriority(), task.getTaskStatus(), task.getSource(),
                task.getDeleted(), task.getPlannedPickupTime(), task.getPlannedDeliveryTime(), task.getCreatedAt());
    }
}
