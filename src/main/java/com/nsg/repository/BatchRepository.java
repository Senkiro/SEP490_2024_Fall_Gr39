package com.nsg.repository;

import com.nsg.entity.BatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BatchRepository extends JpaRepository<BatchEntity, String> {
    Optional<BatchEntity> findByBatchName(String batchName);

    void deleteByBatchName(String batchName);
}
