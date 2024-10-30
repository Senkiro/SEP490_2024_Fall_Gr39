package com.nsg.controller;

import com.nsg.dto.request.batch.BatchCreationRequest;
import com.nsg.dto.response.ApiResponse;
import com.nsg.entity.BatchEntity;
import com.nsg.service.BatchService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/staff")
//@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StaffController {
    @Autowired
    BatchService batchService;

    //show batch list
    @GetMapping("/batch")
    ApiResponse<List<BatchEntity>> getAllBatch(){
        ApiResponse<List<BatchEntity>> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setResult(batchService.getAllBatch());
        return apiResponse;
    }

    //create new batch
    @PostMapping("/save-batch")
    ApiResponse<BatchEntity> saveBatch(@RequestBody @Valid BatchCreationRequest request){
        ApiResponse<BatchEntity> apiResponse = new ApiResponse<>();

        batchService.saveBatch(request);
        BatchEntity batch = batchService.getBatch(request.getBatchName());

        apiResponse.setCode(1000);
        apiResponse.setResult(batch);
        return apiResponse;
    }

}
