package com.nsg.service;

import com.nsg.dto.request.batch.BatchCreationRequest;
import com.nsg.dto.response.batch.BatchResponse;
import com.nsg.entity.BatchEntity;
import com.nsg.entity.EventEntity;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BatchService {
    //save batchEntity
    void saveBatch(BatchCreationRequest batchCreationRequest);

    //get all batchEntity
    List<BatchEntity> getAllBatch();

    //update batchEntity
    BatchEntity updateBatch(String batchName, BatchCreationRequest request);

    //delete batchEntity
    void deleteBatch(String batchId);

    //get by batchName
    BatchEntity getBatch(String batchName);

    Page<BatchResponse> getBatches(int page, int size);

    Page<BatchEntity> findBatchsByName(String batchName, int page, int size);
}
