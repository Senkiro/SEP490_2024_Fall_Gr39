package com.nsg.service.imp;

import com.nsg.Mapper.BatchMapper;
import com.nsg.dto.request.batch.BatchCreationRequest;
import com.nsg.entity.Batch;
import com.nsg.repository.BatchRepository;
import com.nsg.service.BatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BatchServiceImp implements BatchService {

    @Autowired
    private final BatchRepository batchRepository;

    @Autowired
    BatchMapper batchMapper;

    @Override
    public void saveBatch(BatchCreationRequest batchCreationRequest) {
        Batch batch = batchMapper.toBatch(batchCreationRequest);
        batchRepository.save(batch);
    }

    @Override
    public List<Batch> getAllBatch() {
        return batchRepository.findAll();
    }

    @Override
    public Batch updateBatch(String batchName) {
        Batch batch = batchRepository.findByBatchName(batchName);
        return batch;
    }

    @Override
    public void deleteBatch(String batchId) {

    }

    @Override
    public Batch getBatch(String batchName) {
        return batchRepository.findByBatchName(batchName);
    }
}
