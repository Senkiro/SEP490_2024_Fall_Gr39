package com.nsg.repository;

import com.nsg.entity.BatchEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchRepository extends BaseRepository<BatchEntity, String> {
    BatchEntity findByBatchName(String batchName);
}
