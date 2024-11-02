package com.nsg.controller;

import com.nsg.dto.request.batch.BatchCreationRequest;
import com.nsg.dto.request.lesson.LessonCreateRequest;
import com.nsg.dto.response.ApiResponse;
import com.nsg.entity.BatchEntity;
import com.nsg.entity.LessonEntity;
import com.nsg.service.BatchService;
import com.nsg.service.LessonService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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

    @Autowired
    LessonService lessonService;

    //get all batch
    @GetMapping("/batch")
    ApiResponse<List<BatchEntity>> getAllBatch(){
        ApiResponse<List<BatchEntity>> apiResponse = new ApiResponse<>();
        List<BatchEntity> entityList = batchService.getAllBatch();
        apiResponse.setResult(entityList);

        return apiResponse;
    }

    //create new batch
    @PostMapping("/save-batch")
    ApiResponse<BatchEntity> saveBatch(@RequestBody @Validated BatchCreationRequest request){
        ApiResponse<BatchEntity> apiResponse = new ApiResponse<>();

        batchService.saveBatch(request);
        BatchEntity batchEntity = batchService.getBatch(request.getBatchName());
        apiResponse.setResult(batchEntity);

        return apiResponse;
    }

    //update
    @PostMapping("/update-batch")
    ApiResponse<BatchEntity> updateBatch(@RequestBody BatchCreationRequest request){
        ApiResponse<BatchEntity> apiResponse = new ApiResponse<>();

        //get batch by batchName
        BatchEntity batch = batchService.updateBatch(request.getBatchName(), request);
        apiResponse.setResult(batch);

        return apiResponse;
    }

    //delete batch
    @DeleteMapping("/delete-batch/{batchName}")
    ApiResponse<?> deleteBatch(@PathVariable("batchName") String batchName){
        ApiResponse<?> apiResponse = new ApiResponse<>();

        //delete by id
        batchService.deleteBatch(batchName);

        apiResponse.setMessage("Delete successfully!");
        return apiResponse;
    }

    //CRUD for lesson
    //get all
    @GetMapping("/lesson")
    ApiResponse<List<LessonEntity>> getAllLesson(){
        ApiResponse<List<LessonEntity>> apiResponse = new ApiResponse<>();

        List<LessonEntity> lessonEntityList = lessonService.getAllLesson();
        apiResponse.setResult(lessonEntityList);

        return  apiResponse;
    }

    //create new lesson
    @PostMapping("/create-lesson")
    ApiResponse<LessonEntity> createLesson(@RequestBody @Valid LessonCreateRequest request) {
        ApiResponse<LessonEntity> apiResponse = new ApiResponse<>();

        lessonService.createLesson(request);
        apiResponse.setMessage("create successfully!");

        return apiResponse;
    }

    //delete lesson
    @DeleteMapping("/lesson/{lessonId}")
    ApiResponse<?> deleteLesson(@PathVariable("lessonId") String lessonId){
        ApiResponse<?> apiResponse = new ApiResponse<>();

        lessonService.deleteLesson(lessonId);
        apiResponse.setMessage("Delete successfully!");

        return  apiResponse;
    }

    //update lesson
    @PostMapping("/update-lesson/{lessonId}")
    ApiResponse<LessonEntity> updateLesson(@PathVariable("lessonId") String lessonId, @RequestBody LessonCreateRequest request) {
        ApiResponse<LessonEntity> apiResponse = new ApiResponse<>();

        LessonEntity lesson = lessonService.updateLesson(lessonId, request);

        apiResponse.setResult(lesson);

        return  apiResponse;
    }

//    @PostMapping("/save-batch")
//    ApiResponse<BatchEntity> saveBatch(@RequestBody @Valid BatchCreationRequest request){
//        ApiResponse<BatchEntity> apiResponse = new ApiResponse<>();
//
//        batchService.saveBatch(request);
//        BatchEntity batch = batchService.getBatch(request.getBatchName());
//
//        apiResponse.setCode(1000);
//        apiResponse.setResult(batch);
//        return apiResponse;
//    }

}
