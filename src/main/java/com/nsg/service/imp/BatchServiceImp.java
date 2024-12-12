package com.nsg.service.imp;

import com.nsg.Mapper.BatchMapper;
import com.nsg.common.exception.AppException;
import com.nsg.common.exception.ErrorCode;
import com.nsg.dto.request.batch.BatchCreationRequest;
import com.nsg.dto.response.batch.BatchResponse;
import com.nsg.entity.BatchEntity;
import com.nsg.entity.ClassEntity;
import com.nsg.entity.EventEntity;
import com.nsg.repository.BatchRepository;
import com.nsg.service.BatchService;
import com.nsg.service.ClassService;
import com.nsg.service.MarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BatchServiceImp implements BatchService {

    @Autowired
    private final BatchRepository batchRepository;

    @Autowired
    BatchMapper batchMapper;

    @Autowired
    MarkService markService;

    @Autowired
    ClassService classService;

    @Override
    public void saveBatch(BatchCreationRequest batchCreationRequest) {
        if (batchRepository.existsByBatchName(batchCreationRequest.getBatchName())) {
            throw new AppException(ErrorCode.BATCH_EXISTED);
        }
        BatchEntity batchEntity = batchMapper.toBatchEntity(batchCreationRequest);

        //set default status is 2 (Not happen)
        batchEntity.setBatchStatus( 2 );

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
        //mapper
        BeanUtils.copyProperties(request, batchEntity);

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
        return batchRepository.findByBatchName(batchName).orElseThrow(
                () -> new AppException(ErrorCode.BATCH_NOT_EXISTED)
        );
    }

    @Override
    public Page<BatchResponse> getBatches(int page, int size) {
        Page<BatchEntity> batchEntities = batchRepository.findAll(PageRequest.of(page, size));
        List<BatchResponse> responseList = totoBatchResponseList(batchEntities.getContent());
        return new PageImpl<>(responseList, batchEntities.getPageable(), batchEntities.getTotalElements());
    }

    public BatchResponse toBatchResponse(BatchEntity batch) {
        BatchResponse batchResponse = new BatchResponse();
        batchResponse.setBatchName(batch.getBatchName());
        batchResponse.setStartTime(batch.getStartTime());
        batchResponse.setEndTime(batch.getEndTime());
        batchResponse.setYear(batch.getYear());
        batchResponse.setBatchStatus( batch.getBatchStatus() );

        return batchResponse;
    }

    public List<BatchResponse> totoBatchResponseList(List<BatchEntity> batchEntities) {
        List<BatchResponse> responseList = new ArrayList<>();

        for (BatchEntity batch : batchEntities) {
            BatchResponse batchResponse = toBatchResponse(batch);
            responseList.add(batchResponse);
        }

        return responseList;
    }

    @Override
    public Page<BatchResponse> findBatchsByName(String batchName, int page, int size) {
        Page<BatchEntity> batchEntities = batchRepository.findByBatchNameContaining(batchName, PageRequest.of(page, size));
        List<BatchResponse> responseList = totoBatchResponseList(batchEntities.getContent());
        return new PageImpl<>(responseList, batchEntities.getPageable(), batchEntities.getTotalElements());
    }

    //change status of batch
    @Override
    public void changeBatchStatus(String batchName) {
        BatchEntity batchEntity = batchRepository.findByBatchName(batchName).orElseThrow(
                () -> new AppException(ErrorCode.BATCH_NOT_EXISTED)
        );

        // 0 -> graduated; 1 -> on progress
        if (batchEntity.getBatchStatus() == 2 || batchEntity.getBatchStatus() == 0) {
            batchEntity.setBatchStatus( 1 );
        }
        batchRepository.save(batchEntity);
    }

    //batch summary
    @Override
    public void batchSummary(String batchName) {
        BatchEntity batchEntity = batchRepository.findByBatchName(batchName).orElseThrow(
                () -> new AppException(ErrorCode.BATCH_NOT_EXISTED)
        );

        //get list of class in batch
        List<ClassEntity> classEntityList = batchEntity.getClassEntityList();

        for (ClassEntity classEntity : classEntityList) {
            if (classEntity.isClassStatus()) {
                //calculate all student's mark of one class
                markService.courseSummary( classEntity.getClassId() );

                //then change status of class to false
                classService.changeClassStatus( classEntity.getClassId() );
            }
        }

        //change batch status to Graduated
        batchEntity.setBatchStatus( 0 );
        batchRepository.save(batchEntity);

    }

}
