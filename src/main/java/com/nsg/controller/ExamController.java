package com.nsg.controller;

import com.nsg.dto.request.exam.ExamRequest;
import com.nsg.dto.request.exam.ExamTypeRequest;
import com.nsg.dto.request.exam.ExamUpdateRequest;
import com.nsg.dto.response.ApiResponse;
import com.nsg.dto.response.exam.ExamResponse;
import com.nsg.dto.response.exam.ExamTypeResponse;
import com.nsg.service.ExamService;
import com.nsg.service.ExamTypeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/staff")
@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@SecurityRequirement(name = "Authorization")
public class ExamController {
    @Autowired
    ExamService examService;

    @Autowired
    ExamTypeService examTypeService;

    /**********************************
     * Manage Exam Type
     **********************************/
    //create exam type
    @PostMapping("/create-exam-type")
    public ApiResponse<ExamTypeResponse> createExamType(@RequestBody ExamTypeRequest request) {
        return ApiResponse.<ExamTypeResponse>builder()
                .result(examTypeService.createExamType(request))
                .message("Create a new exam type rate successfully")
                .build();
    }

    //get all exam type
    @GetMapping("/get-all-exam-type")
    public ApiResponse<List<ExamTypeResponse>> getAllExamType() {
        return ApiResponse.<List<ExamTypeResponse>>builder()
                .result(examTypeService.getAllExamType())
                .build();
    }

    //get exam type by examType
    @GetMapping("/get-exam-type")
    public ApiResponse<ExamTypeResponse> getExamType(@RequestParam int examType) {
        return ApiResponse.<ExamTypeResponse>builder()
                .result(examTypeService.getExamType(examType))
                .build();
    }

    //update exam type
    @PostMapping("/update-exam-type")
    public ApiResponse<ExamTypeResponse> updateExamType(@RequestParam int examType, @RequestBody ExamTypeRequest request) {
        return ApiResponse.<ExamTypeResponse>builder()
                .result(examTypeService.updateExamType(examType, request))
                .message("Update exam type successfully!")
                .build();
    }

    //delete exam type
    @DeleteMapping("/delete-exam-type")
    public ApiResponse<?> deleteExamType(@RequestParam int examType) {
        examTypeService.deleteExamType(examType);
        return ApiResponse.builder()
                .message("Delete exam type successfully!")
                .build();
    }

    /**********************************
     * Manage Exam
     **********************************/

    //create new exam
    @PostMapping("/create-exam")
    public ApiResponse<?> createExam(@RequestBody ExamRequest request) {
        examService.createExam(request);
        return ApiResponse.builder()
                .message("Create new exam successfully!")
                .build();
    }

    //get all exam
    @GetMapping("/get-all-exam")
    public ApiResponse<List<ExamResponse>> getAllExam() {
        return ApiResponse.<List<ExamResponse>>builder()
                .result(examService.getAllExam())
                .build();
    }

    //get exam by id
    @GetMapping("/get-exam/{exam_id}")
    public ApiResponse<ExamResponse> getExam(@PathVariable("exam_id") String exam_id) {
        return ApiResponse.<ExamResponse>builder()
                .result(examService.getExam(exam_id))
                .build();
    }

    //update
    @PostMapping("/update-exam/{exam_id}")
    public ApiResponse<ExamResponse> updateExam(@PathVariable("exam_id") String exam_id, @RequestBody ExamUpdateRequest request) {
        return ApiResponse.<ExamResponse>builder()
                .result(examService.updateExam(exam_id, request))
                .message("Update exam successfully!")
                .build();
    }

    //delete
    @DeleteMapping("/delete-exam/{exam_id}")
    public ApiResponse<?> deleteExam(@PathVariable("exam_id") String exam_id) {
        examService.deleteExam(exam_id);
        return ApiResponse.builder()
                .message("Delete exam successfully!")
                .build();
    }
}
