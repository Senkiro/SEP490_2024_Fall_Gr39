package com.nsg.service.imp;

import com.nsg.Mapper.ExamTypeMapper;
import com.nsg.common.exception.AppException;
import com.nsg.common.exception.ErrorCode;
import com.nsg.dto.request.exam.ExamTypeRequest;
import com.nsg.dto.response.exam.ExamTypeResponse;
import com.nsg.entity.ExamTypeRateEntity;
import com.nsg.repository.ExamTypeRepository;
import com.nsg.service.ExamTypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExamTypeServiceImp implements ExamTypeService {

    @Autowired
    ExamTypeRepository examTypeRepository;

    @Override
    public ExamTypeResponse createExamType(ExamTypeRequest request) {
        ExamTypeRateEntity examTypeRate = new ExamTypeRateEntity();
        BeanUtils.copyProperties(request, examTypeRate);

        return ExamTypeMapper.INSTANCE.toExamTypeResponse(examTypeRepository.save(examTypeRate));
    }

    @Override
    public List<ExamTypeResponse> getAllExamType() {
        List<ExamTypeResponse> listResponse = new ArrayList<>();
        List<ExamTypeRateEntity> examTypeRateEntityList = examTypeRepository.findAll();

        for (ExamTypeRateEntity examTypeRate : examTypeRateEntityList) {

            ExamTypeResponse temp = new ExamTypeResponse();
            temp.setExamType(examTypeRate.getExamType());
            temp.setExamName(examTypeRate.getExamName());
            temp.setExamRate(examTypeRate.getExamRate());

            listResponse.add(temp);

        }

        return listResponse;
    }

    @Override
    public ExamTypeResponse getExamType(int examType) {
        return ExamTypeMapper.INSTANCE.toExamTypeResponse(examTypeRepository.findByExamType(examType).orElseThrow(
                () -> new AppException(ErrorCode.EXAM_TYPE_NOT_FOUND)
        ));
    }

    @Override
    public ExamTypeResponse updateExamType(int examType, ExamTypeRequest request) {
        ExamTypeRateEntity examTypeRate = examTypeRepository.findByExamType(examType).orElseThrow(
                () -> new AppException(ErrorCode.EXAM_TYPE_NOT_FOUND)
        );

        examTypeRate.setExamName(request.getExamName());
        examTypeRate.setExamRate(request.getExamRate());

        return ExamTypeMapper.INSTANCE.toExamTypeResponse(examTypeRepository.save(examTypeRate));
    }

    @Override
    public void deleteExamType(int examType) {
        String examTypeId = String.valueOf(examType);
        examTypeRepository.deleteById(examTypeId);
    }
}
