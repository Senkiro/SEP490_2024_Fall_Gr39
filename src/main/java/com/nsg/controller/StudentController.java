package com.nsg.controller;

import com.nsg.dto.response.ApiResponse;
import com.nsg.dto.response.news.NewsResponse;
import com.nsg.dto.response.student.StudentResponse;
import com.nsg.entity.NewEntity;
import com.nsg.service.NewsService;
import com.nsg.service.StudentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student")
@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@SecurityRequirement(name = "Authorization")
public class StudentController {
    @Autowired
    StudentService studentService;

    @Autowired
    NewsService newsService;

    //Xem học sinh lớp mình
    @GetMapping("/get-student-class")
    public ApiResponse<Page<StudentResponse>> viewStudentByClassName(@RequestParam int page,
                                                                                 @RequestParam int size,
                                                                                 @RequestParam String studentId) {
        Page<StudentResponse> studentList = studentService.getStudentByClass(page, size, studentId);
        return ApiResponse.<Page<StudentResponse>>builder()
                .result(studentList)
                .build();
    }

    //get publish news
    @GetMapping("/get-all-publish-news")
    public ApiResponse<Page<NewsResponse>> getAllPublishNews(@RequestParam int page, @RequestParam int size) {
        return ApiResponse.<Page<NewsResponse>>builder()
                .result(newsService.getAllPublishNews(page, size))
                .build();
    }

    // getNewsById
    @GetMapping("/get-news")
    public ApiResponse<NewEntity> getNewsById(@RequestParam String newsId) {
        NewEntity newEntity = newsService.getNewtById(newsId);
        return ApiResponse.<NewEntity>builder()
                .result(newEntity)
                .build();
    }
}
