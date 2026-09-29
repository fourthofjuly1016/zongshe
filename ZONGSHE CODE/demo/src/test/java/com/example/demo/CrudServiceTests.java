package com.example.demo;

import com.example.demo.dto.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.service.CarService;
import com.example.demo.service.GoodsService;
import com.example.demo.service.PoiService;
import com.example.demo.service.RoadService;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class CrudServiceTests {

    @Autowired
    private CarService carService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private PoiService poiService;

    @Autowired
    private RoadService roadService;

    @Autowired
    private Validator validator;

    @Test
    void supportsCrudAndRejectsSameRoadEndpoints() {
        CarResponse car = carService.create(new CarRequest(
                "TEST-001", "IDLE", "VAN", 10,
                new BigDecimal("3000"), new BigDecimal("15")));
        assertThat(carService.findById(car.carId()).plateNumber()).isEqualTo("TEST-001");

        GoodsResponse goods = goodsService.create(new GoodsRequest(
                "测试货物", "GENERAL", new BigDecimal("100"), new BigDecimal("2"), false, 3));
        assertThat(goodsService.findById(goods.goodsId()).name()).isEqualTo("测试货物");

        PoiResponse start = poiService.create(new PoiRequest(
                "测试起点", "WAREHOUSE", "成都市测试路1号",
                new BigDecimal("30.5728000"), new BigDecimal("104.0668000"), 20));
        PoiResponse end = poiService.create(new PoiRequest(
                "测试终点", "CUSTOMER", "成都市测试路2号",
                new BigDecimal("30.6628000"), new BigDecimal("104.0768000"), 10));

        RoadResponse road = roadService.create(new RoadRequest(
                "测试道路", start.poiId(), end.poiId(), new BigDecimal("12.50"),
                "OPEN", "LOW", new BigDecimal("1.00"), new BigDecimal("5.00")));
        assertThat(roadService.findById(road.roadId()).endPoiId()).isEqualTo(end.poiId());

        RoadRequest invalidRoad = new RoadRequest(
                "无效道路", start.poiId(), start.poiId(), BigDecimal.ONE,
                "OPEN", "LOW", BigDecimal.ZERO, BigDecimal.ZERO);
        assertThat(validator.validate(invalidRoad)).isNotEmpty();

        roadService.delete(road.roadId());
        carService.delete(car.carId());
        assertThatThrownBy(() -> carService.findById(car.carId()))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
