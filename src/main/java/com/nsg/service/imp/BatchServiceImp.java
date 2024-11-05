package com.nsg.service.imp;

import com.nsg.Mapper.BatchMapper;
import com.nsg.common.exception.AppException;
import com.nsg.common.exception.ErrorCode;
import com.nsg.dto.request.batch.BatchCreationRequest;
import com.nsg.entity.BatchEntity;
import com.nsg.repository.BatchRepository;
import com.nsg.service.BatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
        BatchEntity batchEntity = batchRepository.findByBatchName(batchName).orElseThrow(
                () -> new AppException(ErrorCode.BATCH_NOT_EXISTED)
        );
        System.out.println(batchEntity);

        //mapper
        batchEntity = batchMapper.toBatchEntity(request);

        //save
        batchRepository.save(batchEntity);

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

    @Override
    public Page<BatchEntity> getBatches(int page, int size) {
        Page<BatchEntity> result = batchRepository.findAll(PageRequest.of(page, size));
        return result;
    }

}
