package com.example.demo.repository;

import com.example.demo.entity.DispatchAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DispatchAssignmentRepository extends JpaRepository<DispatchAssignment, Long> {
    Optional<DispatchAssignment> findByAssignmentId(Long id);
    boolean existsByTask_TaskIdAndDispatchStatusIn(Long taskId, java.util.Collection<String> statuses);
}
