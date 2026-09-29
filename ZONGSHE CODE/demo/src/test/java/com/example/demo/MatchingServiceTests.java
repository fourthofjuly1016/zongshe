package com.example.demo;

import com.example.demo.entity.Car;
import com.example.demo.entity.Goods;
import com.example.demo.entity.Poi;
import com.example.demo.entity.TransportTask;
import com.example.demo.service.MatchingService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class MatchingServiceTests {
    @Test
    void rejectsVehicleWhenCapacityIsInsufficient() {
        Goods goods = new Goods();
        goods.setGoodsWeight(new BigDecimal("1000"));
        goods.setGoodsVolume(new BigDecimal("10"));
        goods.setGoodsIsdangerous(false);
        goods.setCategory("GENERAL");

        TransportTask task = new TransportTask();
        task.setTaskId(1L);
        task.setGoods(goods);
        task.setQuantity(new BigDecimal("2"));
        task.setPriority(3);

        Car car = new Car();
        car.setCarId(1);
        car.setCarType("VAN");
        car.setCarStatus("IDLE");
        car.setDeleted(false);
        car.setMaxWeight(new BigDecimal("1500"));
        car.setMaxVolume(new BigDecimal("30"));

        var result = new MatchingService().score(task, car);
        assertThat(result.eligible()).isFalse();
        assertThat(result.reasons()).contains("车辆载重不足");
    }

    @Test
    void rejectsVehicleWhenItIsAlreadyAssigned() {
        Goods goods = new Goods();
        goods.setGoodsWeight(new BigDecimal("100"));
        goods.setGoodsVolume(new BigDecimal("2"));
        goods.setGoodsIsdangerous(false);
        goods.setCategory("GENERAL");

        TransportTask task = new TransportTask();
        task.setTaskId(2L);
        task.setGoods(goods);
        task.setQuantity(BigDecimal.ONE);
        task.setPriority(3);

        Car car = new Car();
        car.setCarId(2);
        car.setCarType("VAN");
        car.setCarStatus("ASSIGNED");
        car.setDeleted(false);
        car.setMaxWeight(new BigDecimal("1000"));
        car.setMaxVolume(new BigDecimal("10"));

        var result = new MatchingService().score(task, car);
        assertThat(result.eligible()).isFalse();
        assertThat(result.reasons()).contains("车辆当前不在空闲状态");
    }
}
