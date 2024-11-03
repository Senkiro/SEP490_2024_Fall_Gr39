package com.nsg.service;

import com.nsg.dto.request.lesson.LessonCreateRequest;
import com.nsg.entity.LessonEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface LessonService {
    //get all lesson
    List<LessonEntity> getAllLesson();

    //create new lesson
    void createLesson(LessonCreateRequest request);

    //update lesson
    LessonEntity updateLesson(String lessonId, LessonCreateRequest request);

    //delete lesson
    void deleteLesson(String lessonId);
}
