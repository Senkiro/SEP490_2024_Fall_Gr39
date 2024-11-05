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
    TeacherService teacherService;

    @Autowired
    LessonService lessonService;

    @Autowired
    UserService userService;


    /**********************************
     * Manage Student
     **********************************/
    //create new student
    @PostMapping("/create-student")
    public ApiResponse<?> createStudent(@RequestBody @Valid @Validated StudentCreattionRequest request){
        return ApiResponse.builder()
                .result(userService.studentCreate(request))
                .build();
    }

    @GetMapping("/student-list")
    public ApiResponse<Page<StudentResponse>> viewStudents(@RequestParam int page, @RequestParam int size){
        Page<StudentResponse> studentList = userService.getAllStudent(page, size);
        return ApiResponse.<Page<StudentResponse>>builder()
                .result(studentList)
                .build();
    }

    @DeleteMapping("/delete-student/{student_id}")
    public ApiResponse<?> deleteStudent(@PathVariable("student_id") String student_id){
        userService.deleteUser(student_id);
        return ApiResponse.builder()
                .message("Delete successful")
                .build();
    }

    /**********************************
     * Manage Batch
     **********************************/

    //get all batch
    @GetMapping("/batch")
    ApiResponse<Page<BatchEntity>> getAllBatch(@RequestParam int page, @RequestParam int size) {
        return ApiResponse.<Page<BatchEntity>>builder()
                .code(1000)
                .result(batchService.getBatches(page, size))
                .build();
    }

    //create new batch
    @PostMapping("/save-batch")
    ApiResponse<BatchEntity> saveBatch(@RequestBody @Validated BatchCreationRequest request){
        batchService.saveBatch(request);
        BatchEntity batchEntity = batchService.getBatch(request.getBatchName());
        return ApiResponse.<BatchEntity>builder()
                .message("A new batch have been created!")
                .result(batchEntity)
                .build();
    }

    //update
    @PostMapping("/update-batch")
    ApiResponse<BatchEntity> updateBatch(@RequestBody BatchCreationRequest request){
        BatchEntity batch = batchService.updateBatch(request.getBatchName(), request);
        return ApiResponse.<BatchEntity>builder()
                .result(batch)
                .build();
    }

    //delete batch
    @DeleteMapping("/delete-batch/{batchName}")
    ApiResponse<?> deleteBatch(@PathVariable("batchName") String batchName){
        batchService.deleteBatch(batchName);
        return ApiResponse.builder()
                .message("Delete successfully!")
                .build();
    }

    /**********************************
     * Manage Lesson
     **********************************/

    //get all
    @GetMapping("/lesson")
    ApiResponse<Page<LessonEntity>> getAllLesson(@RequestParam int page, @RequestParam int size){
        Page<LessonEntity> lessonEntityList = lessonService.getLessons(page,size);
        return  ApiResponse.<Page<LessonEntity>>builder()
                .result(lessonEntityList)
                .build();
    }

    //create new lesson
    @PostMapping("/create-lesson")
    ApiResponse<LessonEntity> createLesson(@RequestBody @Valid LessonCreateRequest request) {
        lessonService.createLesson(request);
        return ApiResponse.<LessonEntity>builder()
                .message("A new lesson have been created!")
                .build();
    }

    //delete lesson
    @DeleteMapping("/lesson/{lessonId}")
    ApiResponse<?> deleteLesson(@PathVariable("lessonId") String lessonId){
        lessonService.deleteLesson(lessonId);
        return ApiResponse.builder()
                .message("Delete lesson successfully!")
                .build();
    }

    //update lesson
    @PostMapping("/update-lesson/{lessonId}")
    ApiResponse<LessonEntity> updateLesson(@PathVariable("lessonId") String lessonId, @RequestBody LessonCreateRequest request) {
        LessonEntity lesson = lessonService.updateLesson(lessonId, request);
        return ApiResponse.<LessonEntity>builder()
                .result(lesson)
                .build();
    }

    /**********************************
     * Manage Teacher
     **********************************/

    //get teacher paginate
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

    //create teacher
    @PostMapping("/create-teacher")
    public ApiResponse<?> createSTeacher(@RequestBody @Valid @Validated UserCreationRequest request){
        UserRole role = UserRole.TEACHER;
        return ApiResponse.builder()
                .result(userService.userCreate(request, role))
                .build();
    }

}
