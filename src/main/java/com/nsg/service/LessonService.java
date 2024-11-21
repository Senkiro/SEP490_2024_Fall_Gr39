package com.nsg.service;

import com.nsg.dto.request.lesson.LessonCreateRequest;
import com.nsg.dto.response.lesson.LessonResponse;
import com.nsg.entity.LessonEntity;
import com.nsg.entity.StudentEntity;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface LessonService {
    //get all lesson
    List<LessonEntity> getAllLesson();

    //get a lesson by id
    LessonResponse getLesson(String lessonId);

    Page<LessonResponse> getLessons(int page, int size);

    //create new lesson
    void createLesson(LessonCreateRequest request);

    //update lesson
    LessonEntity updateLesson(String lessonId, LessonCreateRequest request);

    //delete lesson
    void deleteLesson(String lessonId);

    void saveAll(List<LessonEntity> lessons);
}
