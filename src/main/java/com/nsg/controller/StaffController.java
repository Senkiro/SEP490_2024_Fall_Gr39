package com.nsg.controller;

import com.nsg.dto.request.batch.BatchCreationRequest;
import com.nsg.dto.response.ApiResponse;
import com.nsg.entity.BatchEntity;
import com.nsg.service.BatchService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/staff")
@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@SecurityRequirement(name = "Authorization")
public class StaffController {
    @Autowired
    BatchService batchService;

    //show batch list
    @GetMapping("/batch")
    ApiResponse<Page<BatchEntity>> getAllBatch(@RequestParam int page, @RequestParam int size) {
        ApiResponse<Page<BatchEntity>> apiResponse = new ApiResponse<>();
        apiResponse.setCode(1000);
        apiResponse.setResult(batchService.getBatches(page, size));
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
