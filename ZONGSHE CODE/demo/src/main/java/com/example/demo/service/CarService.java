package com.example.demo.service;

import com.example.demo.dto.CarRequest;
import com.example.demo.dto.CarResponse;
import com.example.demo.entity.Car;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.CarRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CarService {

    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<CarResponse> findAll() {
        return carRepository.findAllByDeletedFalse(Sort.by("carId")).stream().map(this::toResponse).toList();
    }

    public CarResponse findById(Integer id) {
        return toResponse(findActiveEntity(id));
    }

    @Transactional
    public CarResponse create(CarRequest request) {
        Car car = apply(new Car(), request);
        car.setDeleted(false);
        return toResponse(carRepository.save(car));
    }

    @Transactional
    public CarResponse update(Integer id, CarRequest request) {
        return toResponse(carRepository.save(apply(findActiveEntity(id), request)));
    }

    @Transactional
    public void delete(Integer id) {
        Car car = findActiveEntity(id);
        car.setDeleted(true);
        car.setCarStatus("INVALID");
        carRepository.save(car);
    }

    public Car findActiveEntity(Integer id) {
        return carRepository.findByCarIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("车辆", id));
    }

    private Car apply(Car car, CarRequest request) {
        car.setPlateNumber(request.plateNumber());
        car.setCarStatus(request.carStatus());
        car.setCarType(request.carType());
        car.setCarStorage(request.carStorage());
        car.setMaxWeight(request.maxWeight());
        car.setMaxVolume(request.maxVolume());
        return car;
    }

    private CarResponse toResponse(Car car) {
        return new CarResponse(car.getCarId(), car.getPlateNumber(), car.getCarStatus(),
                car.getCarType(), car.getCarStorage(), car.getMaxWeight(), car.getMaxVolume(), car.getDeleted());
    }
}
