package com.nsg.controller;

import com.nsg.dto.request.batch.BatchCreationRequest;
import com.nsg.dto.response.ApiResponse;
import com.nsg.dto.response.batch.BatchResponse;
import com.nsg.entity.BatchEntity;
import com.nsg.service.BatchService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/staff")
@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@SecurityRequirement(name = "Authorization")
public class BatchController {
    @Autowired
    BatchService batchService;

    /**********************************
     * Manage Batch
     **********************************/

    //get all batch
    @GetMapping("/batch")
    ApiResponse<Page<BatchResponse>> getAllBatch(@RequestParam int page, @RequestParam int size) {
        return ApiResponse.<Page<BatchResponse>>builder()
                .code(1000)
                .result(batchService.getBatches(page, size))
                .build();
    }

    @GetMapping("/search-batch")
    public ApiResponse<Page<BatchResponse>> searchBatchByName(@RequestParam String name, @RequestParam int page, @RequestParam int size) {
        Page<BatchResponse> batchEntityList = batchService.findBatchsByName(name, page, size);
        return ApiResponse.<Page<BatchResponse>>builder()
                .result(batchEntityList)
                .build();
    }

    //get batch by batch name
    @GetMapping("/batch/by-name")
    ApiResponse<BatchEntity> getBatchByName(@RequestParam String batchName) {
        ApiResponse<BatchEntity> apiResponse = new ApiResponse<>();
        BatchEntity batchEntity = batchService.getBatch(batchName);

        if (batchEntity != null) {
            apiResponse.setCode(1000);
            apiResponse.setResult(batchEntity);
        } else {
            apiResponse.setCode(1017);
            apiResponse.setMessage("Batch not found with name: " + batchName);
        }

        return apiResponse;
    }

    //create new batch
    @PostMapping("/save-batch")
    ApiResponse<BatchEntity> saveBatch(@RequestBody @Validated BatchCreationRequest request) {
        batchService.saveBatch(request);
        BatchEntity batchEntity = batchService.getBatch(request.getBatchName());
        return ApiResponse.<BatchEntity>builder()
                .message("A new batch have been created!")
                .result(batchEntity)
                .build();
    }

    //update
    @PostMapping("/update-batch")
    ApiResponse<BatchEntity> updateBatch(@RequestBody BatchCreationRequest request) {
        BatchEntity batch = batchService.updateBatch(request.getBatchName(), request);
        return ApiResponse.<BatchEntity>builder()
                .result(batch)
                .build();
    }

    //delete batch
    @DeleteMapping("/delete-batch/{batchName}")
    ApiResponse<?> deleteBatch(@PathVariable("batchName") String batchName) {
        batchService.deleteBatch(batchName);
        return ApiResponse.builder()
                .message("Delete successfully!")
                .build();
    }

    //change batch status
    @GetMapping("/change-batch-status")
    public ApiResponse<?> changeBatchStatus(@RequestParam String batch_name) {
        batchService.changeBatchStatus(batch_name);
        return ApiResponse.builder()
                .message("Batch status have been changed!")
                .build();
    }

}
