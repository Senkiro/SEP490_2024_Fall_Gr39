package com.nsg.service.imp;

import com.nsg.Mapper.LessonMapper;
import com.nsg.dto.request.lesson.LessonCreateRequest;
import com.nsg.entity.LessonEntity;
import com.nsg.repository.LessonRepository;
import com.nsg.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LessonServiceImp implements LessonService {
    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private LessonMapper lessonMapper;

    @Override
    public List<LessonEntity> getAllLesson() {
        return lessonRepository.findAll();
    }

    @Override
    public void createLesson(LessonCreateRequest request) {
        LessonEntity lesson = lessonMapper.toLessonEntity(request);
        lessonRepository.save(lesson);
    }

    @Override
    public LessonEntity updateLesson(String lessonId, LessonCreateRequest request) {
        //find lesson by id
        LessonEntity lesson = lessonRepository.findById(lessonId).orElse(null);

        if (lesson == null){

        }else {
            //mapping
            lesson = lessonMapper.toLessonEntity(request);

            //save new lesson
            lessonRepository.save(lesson);
        }

        return lesson;
    }

    @Override
    public void deleteLesson(String lessonId) {
        lessonRepository.deleteById(lessonId);
    }
}
