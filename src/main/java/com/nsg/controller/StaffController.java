package com.nsg.controller;

import com.nsg.dto.request.batch.BatchCreationRequest;
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
    //test batchEntity
    @GetMapping("/batch")
    List<BatchEntity> getAllBatch(){
        return batchService.getAllBatch();
    }

    @PostMapping("/create-batch")
    BatchEntity saveBatch(@RequestBody BatchCreationRequest request){
        batchService.saveBatch(request);
        BatchEntity batchEntity = batchService.getBatch(request.getBatchName());
        return batchEntity;
    }
}
