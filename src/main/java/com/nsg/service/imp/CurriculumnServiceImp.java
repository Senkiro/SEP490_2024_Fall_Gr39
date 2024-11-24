package com.nsg.service.imp;

import com.nsg.Mapper.ExamMapper;
import com.nsg.Mapper.LessonMapper;
import com.nsg.dto.response.curriculumn.CurriculumnResponse;
import com.nsg.dto.response.exam.ExamResponse;
import com.nsg.dto.response.lesson.LessonResponse;
import com.nsg.entity.CurriculumnEntity;
import com.nsg.entity.ExamEntity;
import com.nsg.entity.LessonEntity;
import com.nsg.repository.CurriculumnRepository;
import com.nsg.service.CurriculumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CurriculumnServiceImp implements CurriculumnService {

    @Autowired
    CurriculumnRepository curriculumnRepository;

    @Override
    public void createCurriculumn(List<CurriculumnEntity> curriculumnEntityList) {
        System.out.println("Curriculumn list: "+curriculumnEntityList);

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

    public CurriculumnResponse toCurriculumnResponse(CurriculumnEntity curriculumnEntity){
        CurriculumnResponse curriculumnResponse = new CurriculumnResponse();

        curriculumnResponse.setCurriculumnId(curriculumnEntity.getCurriculumnId());
        curriculumnResponse.setSessionNumber(curriculumnEntity.getSessionNumber());

        curriculumnResponse.setLessonResponse(LessonMapper.INSTANCE.toLessonResponse(curriculumnEntity.getLessonEntity()));

        curriculumnResponse.setExamResponse(ExamMapper.INSTANCE.toExamResponse(curriculumnEntity.getExamEntity()));

        return curriculumnResponse;
    }
}
