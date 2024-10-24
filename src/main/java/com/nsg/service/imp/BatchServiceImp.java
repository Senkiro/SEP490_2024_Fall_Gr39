package com.nsg.service.imp;

import com.nsg.Mapper.BatchMapper;
import com.nsg.dto.request.batch.BatchCreationRequest;
import com.nsg.entity.BatchEntity;
import com.nsg.repository.BatchRepository;
import com.nsg.service.BatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BatchServiceImp implements BatchService {

    @Autowired
    private final BatchRepository batchRepository;

    @Autowired
    BatchMapper batchMapper;

    @Override
    public void saveBatch(BatchCreationRequest batchCreationRequest) {
        BatchEntity batchEntity = batchMapper.toBatchEntity(batchCreationRequest);
        batchRepository.save(batchEntity);
    }

    @Override
    public List<BatchEntity> getAllBatch() {
        return batchRepository.findAll();
    }

    @Override
    public BatchEntity updateBatch(String batchName, BatchCreationRequest request) {
        BatchEntity batchEntity = batchRepository.findByBatchName(batchName).orElse(null);

        if (batchEntity == null){
            System.out.println("Batch not found!");
        }else {
            //mapper
            batchEntity = batchMapper.toBatchEntity(request);

            //save
            batchRepository.save(batchEntity);
        }

        return batchEntity;
    }

    @Override
    public void deleteBatch(String batchName) {
        batchRepository.deleteByBatchName(batchName);
    }

    @Override
    public BatchEntity getBatch(String batchName) {
        BatchEntity batch = batchRepository.findByBatchName(batchName).orElse(null);
        return batch;
    }
}
