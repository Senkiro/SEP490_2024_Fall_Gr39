package com.nsg.service.imp;

import com.nsg.Mapper.LessonMapper;
import com.nsg.common.exception.AppException;
import com.nsg.common.exception.ErrorCode;
import com.nsg.dto.request.lesson.LessonCreateRequest;
import com.nsg.dto.response.lesson.LessonResponse;
import com.nsg.entity.LessonEntity;
import com.nsg.repository.LessonRepository;
import com.nsg.service.LessonService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    public LessonResponse getLesson(String lessonId) {
        LessonEntity lesson = lessonRepository.findById(lessonId).orElseThrow(
                () -> new AppException(ErrorCode.LESSON_NOT_FOUND)
        );

        LessonResponse response = LessonMapper.INSTANCE.toLessonResponse(lesson);

        return response;
    }

    @Override
    public Page<LessonResponse> getLessons(int page, int size){

        Page<LessonEntity> lessonEntityPage = lessonRepository.findAll(PageRequest.of(page, size));

        List<LessonResponse> lessonResponseList = new ArrayList<>();

        for (LessonEntity lesson : lessonEntityPage) {
            LessonResponse tempResponse = new LessonResponse();

            tempResponse.setLessonId(String.valueOf(lesson.getLessonId()));
            tempResponse.setLessonTitle(lesson.getLessonTitle());
            tempResponse.setVocabulary(lesson.getVocabulary());
            tempResponse.setKanji(lesson.getKanji());
            tempResponse.setGrammar(lesson.getGrammar());

            lessonResponseList.add(tempResponse);
        }

        return new PageImpl<>(lessonResponseList, lessonEntityPage.getPageable(), lessonEntityPage.getTotalElements());
    }

    @Override
    public void createLesson(LessonCreateRequest request) {
        LessonEntity lesson = LessonMapper.INSTANCE.toLessonEntity(request);
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

    @Override
    public void saveAll(List<LessonEntity> lessons) {
        lessonRepository.saveAll(lessons);
    }
}
