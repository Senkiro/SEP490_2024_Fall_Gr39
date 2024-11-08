package com.nsg.service;

import com.nsg.dto.request.exam.ExamTypeRequest;
import com.nsg.dto.response.exam.ExamTypeResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ExamTypeService {

    //create exam type
    ExamTypeResponse createExamType(ExamTypeRequest request);

    //get all exam type
    List<ExamTypeResponse> getAllExamType();

    //get a exam type by: examType
    ExamTypeResponse getExamType(int examType);

    //update by: examType
    ExamTypeResponse updateExamType(int examType, ExamTypeRequest request);

    //delete by: examType
    void deleteExamType(int examType);

}
