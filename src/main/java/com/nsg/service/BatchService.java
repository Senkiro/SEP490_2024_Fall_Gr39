package com.nsg.service;

import com.nsg.dto.request.batch.BatchCreationRequest;
import com.nsg.entity.BatchEntity;
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
}
