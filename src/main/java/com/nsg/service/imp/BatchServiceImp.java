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
        BatchEntity batch =
                batchMapper.toBatchEntity(batchCreationRequest);

        batchRepository.save(batch);
    }

    @Override
    public List<BatchEntity> getAllBatch() {
        return batchRepository.findAll();
    }

    @Override
    public BatchEntity updateBatch(String batchName) {
        BatchEntity batchEntity = batchRepository.findByBatchName(batchName);
        return batchEntity;
    }

    @Override
    public void deleteBatch(String batchId) {

    }

    @Override
    public BatchEntity getBatch(String batchName) {
        return batchRepository.findByBatchName(batchName);
    }
}
