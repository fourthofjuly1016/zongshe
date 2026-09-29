package com.example.demo.service;

import com.example.demo.dto.GoodsRequest;
import com.example.demo.dto.GoodsResponse;
import com.example.demo.entity.Goods;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.GoodsRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class GoodsService {
    private final GoodsRepository goodsRepository;

    public GoodsService(GoodsRepository goodsRepository) {
        this.goodsRepository = goodsRepository;
    }

    public List<GoodsResponse> findAll() {
        return goodsRepository.findAll(Sort.by("goodsId")).stream().map(this::toResponse).toList();
    }

    public GoodsResponse findById(Integer id) {
        return toResponse(findEntity(id));
    }

    @Transactional
    public GoodsResponse create(GoodsRequest request) {
        return toResponse(goodsRepository.save(apply(new Goods(), request)));
    }

    @Transactional
    public GoodsResponse update(Integer id, GoodsRequest request) {
        return toResponse(goodsRepository.save(apply(findEntity(id), request)));
    }

    @Transactional
    public void delete(Integer id) {
        goodsRepository.delete(findEntity(id));
    }

    private Goods findEntity(Integer id) {
        return goodsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("货物", id));
    }

    private Goods apply(Goods goods, GoodsRequest request) {
        goods.setName(request.name());
        goods.setCategory(request.category());
        goods.setGoodsWeight(request.goodsWeight());
        goods.setGoodsVolume(request.goodsVolume());
        goods.setGoodsIsdangerous(request.goodsIsdangerous());
        goods.setGoodsPriority(request.goodsPriority());
        return goods;
    }

    private GoodsResponse toResponse(Goods goods) {
        return new GoodsResponse(goods.getGoodsId(), goods.getName(), goods.getCategory(),
                goods.getGoodsWeight(), goods.getGoodsVolume(), goods.getGoodsIsdangerous(),
                goods.getGoodsPriority());
    }
}
