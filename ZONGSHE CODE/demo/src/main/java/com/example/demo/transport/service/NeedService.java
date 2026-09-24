package com.example.demo.transport.service;

import com.example.demo.transport.common.BusinessException;
import com.example.demo.transport.dto.NeedCreateDTO;
import com.example.demo.transport.entity.Need;
import com.example.demo.transport.repository.NeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NeedService {

    private final NeedRepository needRepository;

    public Need create(NeedCreateDTO dto) {
        Need n = new Need();
        n.setCargoName(dto.getCargoName());
        n.setCargoQuantity(dto.getCargoQuantity());
        n.setNeedStatus("VALID");
        return needRepository.save(n);
    }

    public void invalidate(Integer id) {
        Need n = needRepository.findById(id)
                .orElseThrow(() -> new BusinessException("需求不存在"));
        n.setNeedStatus("INVALID");
        needRepository.save(n);
    }
}