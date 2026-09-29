package com.example.demo.repository;

import com.example.demo.entity.Car;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Integer> {
    List<Car> findAllByDeletedFalse(Sort sort);

    Optional<Car> findByCarIdAndDeletedFalse(Integer id);
}
