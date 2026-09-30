package com.example.demo.transport.repository;

import com.example.demo.transport.entity.Need;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NeedRepository extends JpaRepository<Need, Integer> {
    List<Need> findByNeedStatus(String needStatus);
}