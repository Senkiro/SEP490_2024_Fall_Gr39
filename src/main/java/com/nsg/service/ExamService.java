package com.nsg.service;

import com.nsg.dto.request.exam.ExamRequest;
import com.nsg.dto.request.exam.ExamUpdateRequest;
import com.nsg.dto.response.exam.ExamResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ExamService {

    //create exam
    void createExam(ExamRequest request, int exam_type);

    //get all exam
    List<ExamResponse> getAllExam();

    //get exam by id
    ExamResponse getExam(String examId);

    //update exam
    ExamResponse updateExam(String examId, ExamUpdateRequest request);

    //delete exam
    void deleteExam(String examId);

}
