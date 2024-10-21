package com.nsg.controller;

import com.nsg.Mapper.BatchMapper;
import com.nsg.dto.request.batch.BatchCreationRequest;
import com.nsg.dto.response.ApiResponse;
import com.nsg.entity.BatchEntity;
import com.nsg.service.BatchService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/staff")
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StaffController {
    @Autowired
    BatchService batchService;

    //get all batch
    @GetMapping("/batch")
    ApiResponse<List<BatchEntity>> getAllBatch(){
        ApiResponse<List<BatchEntity>> apiResponse = new ApiResponse<>();
        List<BatchEntity> entityList = batchService.getAllBatch();
        apiResponse.setResult(entityList);

        return apiResponse;
    }

    //create new batch
    @PostMapping("/create-batch")
    ApiResponse<BatchEntity> saveBatch(@RequestBody BatchCreationRequest request){
        ApiResponse<BatchEntity> apiResponse = new ApiResponse<>();

        batchService.saveBatch(request);
        BatchEntity batchEntity = batchService.getBatch(request.getBatchName());
        apiResponse.setResult(batchEntity);

        return apiResponse;
    }

    //delete
    @PostMapping("/update-batch")
    ApiResponse<BatchEntity> updateBatch(@RequestBody BatchCreationRequest request){
        ApiResponse<BatchEntity> apiResponse = new ApiResponse<>();

        //get batch by batchName
        BatchEntity batch = batchService.updateBatch(request.getBatchName(), request);
        apiResponse.setResult(batch);

        return apiResponse;
    }

}
