package com.nsg.service;

import com.nsg.dto.request.exam.ExamRequest;
import com.nsg.dto.request.exam.ExamUpdateRequest;
import com.nsg.dto.response.exam.ExamResponse;
import com.nsg.dto.response.exam.ExamResponseForMark;
import com.nsg.entity.ExamEntity;
import com.nsg.entity.StudentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ExamService {

    //create exam
    void createExam(ExamRequest request);

    //get all exam
    List<ExamResponse> getAllExam();

    //get exam by id
    ExamResponse getExam(String examId);

    //update exam
    ExamResponse updateExam(String examId, ExamUpdateRequest request);

    //delete exam
    void deleteExam(String examId);

    void saveAll(List<ExamEntity> exams);

    Page<ExamResponseForMark> getExamsByStudent(String studentId, int page, int size);

    List<ExamEntity> getExamByClassId(String classId);
}
