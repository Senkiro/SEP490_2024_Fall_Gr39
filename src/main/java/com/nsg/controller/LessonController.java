package com.nsg.controller;

import com.nsg.dto.request.lesson.LessonCreateRequest;
import com.nsg.dto.response.ApiResponse;
import com.nsg.dto.response.lesson.LessonResponse;
import com.nsg.entity.LessonEntity;
import com.nsg.service.LessonService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/staff")
@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@SecurityRequirement(name = "Authorization")
public class LessonController {
    @Autowired
    LessonService lessonService;

    /**********************************
     * Manage Lesson
     **********************************/

    //get all
    @GetMapping("/lesson")
    ApiResponse<Page<LessonResponse>> getAllLesson(@RequestParam int page, @RequestParam int size) {
        Page<LessonResponse> lessonEntityList = lessonService.getLessons(page, size);
        return ApiResponse.<Page<LessonResponse>>builder()
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

    //get a lesson by id
    @GetMapping("/get-lesson/{lesson_id}")
    ApiResponse<LessonResponse> getLesson(@PathVariable("lesson_id") String lesson_id) {
        return ApiResponse.<LessonResponse>builder()
                .result(lessonService.getLesson(lesson_id))
                .build();
    }

    //delete lesson
    @DeleteMapping("/lesson/{lessonId}")
    ApiResponse<?> deleteLesson(@PathVariable("lessonId") String lessonId) {
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
}
