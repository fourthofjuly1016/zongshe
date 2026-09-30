package com.example.demo.service;

import com.example.demo.common.BusinessException;
import com.example.demo.dto.NeedCreateDTO;
import com.example.demo.entity.Need;
import com.example.demo.repository.NeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<Need> list() {
        return needRepository.findAll();
    }
}