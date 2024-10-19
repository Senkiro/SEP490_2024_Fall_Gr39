package com.nsg.repository;

import com.nsg.entity.Batch;
import com.nsg.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BatchRepository extends BaseRepository<Batch, String> {
    Batch findByBatchName(String batchName);
}
