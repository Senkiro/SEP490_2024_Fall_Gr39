package com.nsg.repository;

import com.nsg.entity.BatchEntity;
import com.nsg.entity.EventEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BatchRepository extends JpaRepository<BatchEntity, String> {
    Optional<BatchEntity> findByBatchName(String batchName);

    boolean existsByBatchName(String batchName);

    void deleteByBatchName(String batchName);

    Page<BatchEntity> findByBatchNameContaining(String name, PageRequest of);

    Optional<BatchEntity> findByBatchStatus(int status);
}
