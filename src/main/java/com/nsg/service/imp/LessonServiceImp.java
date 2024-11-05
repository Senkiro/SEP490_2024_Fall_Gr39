package com.nsg.service.imp;

import com.nsg.Mapper.LessonMapper;
import com.nsg.common.exception.AppException;
import com.nsg.common.exception.ErrorCode;
import com.nsg.dto.request.lesson.LessonCreateRequest;
import com.nsg.entity.LessonEntity;
import com.nsg.repository.LessonRepository;
import com.nsg.service.LessonService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Page<LessonEntity> getLessons(int page, int size){
        return lessonRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public void createLesson(LessonCreateRequest request) {
        LessonEntity lesson = lessonMapper.toLessonEntity(request);
        lessonRepository.save(lesson);
    }

    @Override
    @Transactional
    public LessonEntity updateLesson(String lessonId, LessonCreateRequest request) {
        //find lesson by id
        LessonEntity lesson = lessonRepository.findById(lessonId).orElseThrow(
                () -> new AppException(ErrorCode.LESSON_NOT_FOUND)
        );

        BeanUtils.copyProperties(request, lesson, "lessonId", "sessionEntityList");

        return lesson;
    }

    @Override
    public void deleteLesson(String lessonId) {
        lessonRepository.deleteById(lessonId);
    }
}
