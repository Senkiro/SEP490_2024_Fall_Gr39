package com.nsg.controller;

import com.nsg.common.enums.UserRole;
import com.nsg.dto.request.batch.BatchCreationRequest;
import com.nsg.dto.request.lesson.LessonCreateRequest;
import com.nsg.dto.request.student.StudentCreattionRequest;
import com.nsg.dto.request.user.UserCreationRequest;
import com.nsg.dto.response.ApiResponse;
import com.nsg.dto.response.staff.StudentResponse;
import com.nsg.entity.BatchEntity;
import com.nsg.entity.LessonEntity;
import com.nsg.entity.UserEntity;
import com.nsg.service.BatchService;
import com.nsg.service.LessonService;
import com.nsg.service.TeacherService;
import com.nsg.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    TeacherService teacherService;

    @Autowired
    LessonService lessonService;

    @Autowired
    UserService userService;

    //create new student
    @PostMapping("/create-student")
    public ApiResponse<?> createStudent(@RequestBody @Valid @Validated StudentCreattionRequest request){
        UserRole role = UserRole.STUDENT;
//        return ApiResponse.ok(userService.studentCreate(request));
        return ApiResponse.builder()
                .result(userService.studentCreate(request))
                .build();
    }

    @GetMapping("/student-list")
    public ApiResponse<List<StudentResponse>> viewStudents(){
        List<StudentResponse> studentList = userService.getAllStudent();
        return ApiResponse.<List<StudentResponse>>builder()
                .result(studentList)
                .build();
    }

    @DeleteMapping("/delete-student/{student_id}")
    public ApiResponse<?> deleteStudent(@PathVariable("student_id") String student_id){
        ApiResponse<?> apiResponse = new ApiResponse<>();

        userService.deleteUser(student_id);

        apiResponse.setMessage("Delete successful");

        return apiResponse;
    }

    //get all batch
    @GetMapping("/batch")
    ApiResponse<Page<BatchEntity>> getAllBatch(@RequestParam int page, @RequestParam int size) {
        ApiResponse<Page<BatchEntity>> apiResponse = new ApiResponse<>();
        apiResponse.setCode(1000);
        apiResponse.setResult(batchService.getBatches(page, size));
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

    @GetMapping("/teacher")
    ApiResponse<Map<String, Object>> getAllTeacher(@RequestParam int page, @RequestParam int size) {
        ApiResponse<Map<String, Object>> apiResponse = new ApiResponse<>();
        Page<UserEntity> teacherPage = teacherService.getTeachers(page, size);
        List<UserEntity> teacherList = teacherPage.getContent();
        Map<String, Object> response = new HashMap<>();
        response.put("teachers", teacherList); // Danh sách giáo viên
        response.put("totalElements", teacherPage.getTotalElements()); // Tổng số giáo viên
        response.put("totalPages", teacherPage.getTotalPages()); // Tổng số trang
        response.put("currentPage", teacherPage.getNumber()); // Trang hiện tại (bắt đầu từ 0)
        response.put("pageSize", teacherPage.getSize()); // Số lượng phần tử trên mỗi trang
        apiResponse.setCode(1000);
        apiResponse.setResult(response);
        return apiResponse;
    }

}
