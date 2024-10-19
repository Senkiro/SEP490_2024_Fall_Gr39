package com.nsg.service;

import com.nsg.dto.request.batch.BatchCreationRequest;
import com.nsg.entity.Batch;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BatchService {
    //save batch
    void saveBatch(BatchCreationRequest batchCreationRequest);


    //get all batch
    List<Batch> getAllBatch();

    //update batch
    Batch updateBatch(String batchName);

    //delete batch
    void deleteBatch(String batchId);

    //get by batchName
    Batch getBatch(String batchName);
}
