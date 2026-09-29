package com.example.demo.repository;

import com.example.demo.entity.TransportTask;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransportTaskRepository extends JpaRepository<TransportTask, Long> {
    List<TransportTask> findAllByDeletedFalse(Sort sort);
    Optional<TransportTask> findByTaskIdAndDeletedFalse(Long id);
}
