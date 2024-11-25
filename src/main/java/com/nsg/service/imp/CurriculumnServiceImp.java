package com.nsg.service.imp;

import com.nsg.Mapper.ExamMapper;
import com.nsg.Mapper.LessonMapper;
import com.nsg.common.exception.AppException;
import com.nsg.common.exception.ErrorCode;
import com.nsg.dto.request.curriculumn.CurriculumnRequest;
import com.nsg.dto.response.curriculumn.CurriculumnListResponse;
import com.nsg.dto.response.curriculumn.CurriculumnResponse;
import com.nsg.dto.response.exam.ExamResponse;
import com.nsg.dto.response.lesson.LessonResponse;
import com.nsg.entity.CurriculumnEntity;
import com.nsg.entity.CurriculumnListEntity;
import com.nsg.entity.ExamEntity;
import com.nsg.entity.LessonEntity;
import com.nsg.repository.CurriculumnListRepository;
import com.nsg.repository.CurriculumnRepository;
import com.nsg.repository.ExamRepository;
import com.nsg.repository.LessonRepository;
import com.nsg.service.CurriculumnListService;
import com.nsg.service.CurriculumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CurriculumnServiceImp implements CurriculumnService {

    @Autowired
    CurriculumnRepository curriculumnRepository;

    @Autowired
    CurriculumnListService curriculumnListService;

    @Autowired
    LessonRepository lessonRepository;

    @Autowired
    ExamRepository examRepository;

    @Autowired
    CurriculumnListRepository curriculumnListRepository;

    @Override
    public void createCurriculumn(List<CurriculumnEntity> curriculumnEntityList) {
        curriculumnRepository.saveAll(curriculumnEntityList);
    }

    @Override
    public List<CurriculumnResponse> getAllCurriculumn() {

        List<CurriculumnEntity> curriculumnEntityList = curriculumnRepository.findAll();

        List<CurriculumnResponse> responseList = new ArrayList<>();

        for (CurriculumnEntity curriculumn : curriculumnEntityList) {
            CurriculumnResponse response = toCurriculumnResponse(curriculumn);
            responseList.add(response);
        }

        return responseList;
    }

    @Override
    public CurriculumnResponse getCurriculumn(String curriculumnId) {

        //get by id
        CurriculumnEntity curriculumn = curriculumnRepository.findById(curriculumnId).orElseThrow(
                () -> new AppException(ErrorCode.CURRICULUMN_NOT_FOUND)
        );

        return toCurriculumnResponse(curriculumn);
    }

    @Override
    public CurriculumnResponse updateCurriculumn(String curriculumnId, CurriculumnRequest request) {

        //get by id
        CurriculumnEntity curriculumn = curriculumnRepository.findById(curriculumnId).orElseThrow(
                () -> new AppException(ErrorCode.CURRICULUMN_NOT_FOUND)
        );

        curriculumn.setSessionNumber(request.getSessionNo());

        if (!request.getLessonId().isEmpty()) {
            LessonEntity lesson = lessonRepository.findById(request.getLessonId()).orElseThrow(
                    () -> new AppException(ErrorCode.LESSON_NOT_FOUND)
            );
            curriculumn.setLessonEntity(lesson);
        }

        if (!request.getExamId().isEmpty()) {
            ExamEntity exam = examRepository.findById(request.getExamId()).orElseThrow(
                    () -> new AppException(ErrorCode.EXAM_NOT_FOUND)
            );
            curriculumn.setExamEntity(exam);
        }

        CurriculumnListEntity curriculumnListEntity = curriculumnListRepository.findById(String.valueOf( request.getCurriculumnListId() )).orElseThrow(
                () -> new AppException(ErrorCode.CURRICULUMN_LIST_NOT_FOUND)
        );
        curriculumn.setCurriculumnListEntity(curriculumnListEntity);

        curriculumnRepository.save(curriculumn);

        return getCurriculumn(curriculumnId);
    }

    @Override
    public void deleteCurriculumn(String curriculumnId) {
        curriculumnRepository.deleteById(curriculumnId);
    }

    @Override
    public CurriculumnResponse toCurriculumnResponse(CurriculumnEntity curriculumnEntity){
        CurriculumnResponse curriculumnResponse = new CurriculumnResponse();

        curriculumnResponse.setCurriculumnId(curriculumnEntity.getCurriculumnId());
        curriculumnResponse.setSessionNumber(curriculumnEntity.getSessionNumber());

        curriculumnResponse.setLessonResponse(LessonMapper.INSTANCE.toLessonResponse(curriculumnEntity.getLessonEntity()));

        curriculumnResponse.setExamResponse(ExamMapper.INSTANCE.toExamResponse(curriculumnEntity.getExamEntity()));

        CurriculumnListEntity curriculumnListEntity = curriculumnEntity.getCurriculumnListEntity();

        curriculumnResponse.setCurriculumnListResponse(curriculumnListService.toCurriculumnListResponse(curriculumnListEntity));

        return curriculumnResponse;
    }
}
