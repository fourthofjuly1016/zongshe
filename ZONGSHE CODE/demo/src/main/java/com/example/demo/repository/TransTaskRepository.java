package com.example.demo.repository;

import com.example.demo.entity.TransTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransTaskRepository extends JpaRepository<TransTask, Integer> {
    List<TransTask> findByTaskStatus(String taskStatus);
}