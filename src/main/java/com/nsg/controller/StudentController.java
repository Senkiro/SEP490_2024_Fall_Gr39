package com.nsg.controller;

import com.nsg.dto.response.ApiResponse;
import com.nsg.dto.response.holiday.HolidayResponse;
import com.nsg.dto.response.news.NewsResponse;
import com.nsg.dto.response.student.StudentResponse;
import com.nsg.entity.EventEntity;
import com.nsg.entity.NewEntity;
import com.nsg.service.EventService;
import com.nsg.service.HolidayService;
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

    @Autowired
    HolidayService holidayService;

    @Autowired
    EventService eventService;

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

    //Get all holiday
    @GetMapping("/get-all-holiday")
    public ApiResponse<Page<HolidayResponse>> getAllHoliday(@RequestParam int page, @RequestParam int size) {
        return ApiResponse.<Page<HolidayResponse>>builder()
                .result(holidayService.getAllHoliday(page, size))
                .build();
    }

    //get all event theo schedule => chu lamf
    @GetMapping("/event")
    public ApiResponse<Page<EventEntity>> getAllEvent(@RequestParam int page, @RequestParam int size) {
        Page<EventEntity> eventEntityList = eventService.getEvents(page, size);
        return ApiResponse.<Page<EventEntity>>builder()
                .result(eventEntityList)
                .build();
    }

    // getEventById
    @GetMapping("/get-event")
    public ApiResponse<EventEntity> getEventById(@RequestParam String eventId) {
        EventEntity eventEntity = eventService.getEventById(eventId);
        return ApiResponse.<EventEntity>builder()
                .result(eventEntity)
                .build();
    }
}
