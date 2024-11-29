package com.nsg.service.imp;

import com.nsg.Mapper.ExamMapper;
import com.nsg.Mapper.ExamTypeMapper;
import com.nsg.common.exception.AppException;
import com.nsg.common.exception.ErrorCode;
import com.nsg.dto.request.exam.ExamRequest;
import com.nsg.dto.request.exam.ExamUpdateRequest;
import com.nsg.dto.response.exam.ExamResponse;
import com.nsg.dto.response.exam.ExamResponseForMark;
import com.nsg.dto.response.exam.ExamTypeResponse;
import com.nsg.entity.AttendanceEntity;
import com.nsg.entity.ExamEntity;
import com.nsg.entity.ExamTypeRateEntity;
import com.nsg.entity.StudentEntity;
import com.nsg.repository.AttendanceRepository;
import com.nsg.repository.ExamRepository;
import com.nsg.service.ExamService;
import com.nsg.service.ExamTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExamServiceImp implements ExamService {

    @Autowired
    ExamRepository examRepository;

    @Autowired
    ExamTypeService examTypeService;

    @Autowired
    AttendanceRepository attendanceRepository;

    @Override
    public void createExam(ExamRequest request) {
        //check exam title existed
        if (examRepository.findByExamTitle(request.getExamTitle()).isPresent()) {
            throw new AppException(ErrorCode.EXAM_TITLE_EXISTED);
        } else {

            //get exam type
            ExamTypeRateEntity examTypeRate = ExamTypeMapper.INSTANCE.toExamTypeRateEntity(examTypeService.getExamType(request.getExam_type()));

            ExamEntity exam = ExamMapper.INSTANCE.toExamEntity(request);
            exam.setExamTypeRateEntity(examTypeRate);

            examRepository.save(exam);
        }

    }

    @Override
    public List<ExamResponse> getAllExam() {
        List<ExamResponse> responseList = new ArrayList<>();
        List<ExamEntity> examEntities = examRepository.findAll();

        for (ExamEntity exam : examEntities) {
            ExamResponse response = new ExamResponse();
            response.setExamId(exam.getExamId());
            response.setExamTitle(exam.getExamTitle());
            response.setExamContent(exam.getExamContent());

            //exam type rate
            //check exam.examtyperate is null?
            if (exam.getExamTypeRateEntity() == null) {
                response.setExamTypeRate(null);
            }else {
                ExamTypeResponse examTypeResponse = examTypeService.getExamType(exam.getExamTypeRateEntity().getExamType());
                response.setExamTypeRate(examTypeResponse);
            }
            responseList.add(response);

        }
        return responseList;
    }

    @Override
    public ExamResponse getExam(String examId) {
        ExamEntity exam = examRepository.findById(examId).orElseThrow(
                () -> new AppException(ErrorCode.EXAM_NOT_FOUND)
        );

        ExamResponse examResponse = new ExamResponse();
        examResponse.setExamId(exam.getExamId());
        examResponse.setExamTitle(exam.getExamTitle());
        examResponse.setExamContent(exam.getExamContent());

        //find exam type rate by id
        ExamTypeResponse examTypeResponse = examTypeService.getExamType(exam.getExamTypeRateEntity().getExamType());
        examResponse.setExamTypeRate(examTypeResponse);

        return examResponse;
    }

    @Override
    public ExamResponse updateExam(String examId, ExamUpdateRequest request) {
        ExamEntity exam = examRepository.findById(examId).orElseThrow(
                () -> new AppException(ErrorCode.EXAM_NOT_FOUND)
        );

        exam.setExamTitle(request.getExamTitle());
        exam.setExamContent(request.getExamContent());

        //get exam type
        ExamTypeRateEntity examTypeRate = ExamTypeMapper.INSTANCE.toExamTypeRateEntity(examTypeService.getExamType(request.getExamType()));
        exam.setExamTypeRateEntity(examTypeRate);

        examRepository.save(exam);

        return getExam(examId);
    }

    @Override
    public void deleteExam(String examId) {
        examRepository.deleteById(examId);

    }

    @Override
    public void saveAll(List<ExamEntity> exams) {

        examRepository.saveAll(exams);
    }

    @Override
    public Page<ExamResponseForMark> getExamsByStudent(String studentId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ExamEntity> listExam = attendanceRepository.findExamsByStudentId(studentId, pageable);
        List<ExamResponseForMark> responseList = new ArrayList<>();
        for (ExamEntity exam : listExam) {
            ExamResponseForMark response = new ExamResponseForMark();
            response.setExamId(exam.getExamId());
            response.setExamTitle(exam.getExamTitle());
            response.setExamContent(exam.getExamContent());
            response.setExamType(exam.getExamTypeRateEntity().getExamType().toString());
            responseList.add(response);
        }

        return new PageImpl<>(responseList, listExam.getPageable(), listExam.getTotalElements());
    }

}
