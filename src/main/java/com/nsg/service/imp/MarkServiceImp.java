package com.nsg.service.imp;

import com.nsg.Mapper.ExamMapper;
import com.nsg.common.exception.AppException;
import com.nsg.common.exception.ErrorCode;
import com.nsg.dto.request.mark.MarkCreationRequest;
import com.nsg.dto.request.mark.MarkUpdateRequest;
import com.nsg.dto.response.mark.MarkResponse;
import com.nsg.entity.ExamEntity;
import com.nsg.entity.MarkEntity;
import com.nsg.entity.StudentEntity;
import com.nsg.repository.ExamRepository;
import com.nsg.repository.MarkRepository;
import com.nsg.repository.StudentRepository;
import com.nsg.service.MarkService;
import com.nsg.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MarkServiceImp implements MarkService {

    @Autowired
    MarkRepository markRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    StudentService studentService;

    @Autowired
    ExamRepository examRepository;

    @Override
    public void createMark(MarkCreationRequest request) {
        MarkEntity markEntity = new MarkEntity();

        //convert
        markEntity.setMark(request.getMark());
        markEntity.setComment(request.getComment());

        //get student and exam
        StudentEntity student = studentRepository.findById(request.getStudentId()).orElseThrow(
                () -> new AppException(ErrorCode.STUDENT_NOT_FOUND)
        );
        markEntity.setStudentEntity(student);

        ExamEntity exam = examRepository.findById(String.valueOf( request.getExamId() )).orElseThrow(
                () -> new AppException(ErrorCode.EXAM_NOT_FOUND)
        );
        markEntity.setExamEntity(exam);

        markRepository.save(markEntity);
    }

    @Override
    public Page<MarkResponse> getAllMark(int page, int size) {
        Page<MarkEntity> markEntities = markRepository.findAll(PageRequest.of(page, size));

        List<MarkResponse> responseList = toMarkResponseList(markEntities.getContent());

        return new PageImpl<>(responseList, markEntities.getPageable(), markEntities.getTotalElements());
    }

    public MarkResponse toMarkResponse(MarkEntity markEntity) {
        MarkResponse markResponse = new MarkResponse();

        markResponse.setMarkId(markEntity.getMarkId());
        markResponse.setMark(markEntity.getMark());
        markResponse.setComment(markEntity.getComment());

        markResponse.setStudentResponse(studentService.convertToStudentResponse(markEntity.getStudentEntity()));

        markResponse.setExamResponse(ExamMapper.INSTANCE.toExamResponse(markEntity.getExamEntity()));

        return markResponse;
    }

    public List<MarkResponse> toMarkResponseList(List<MarkEntity> markEntityList) {
        List<MarkResponse> responseList = new ArrayList<>();

        for (MarkEntity mark : markEntityList) {
            MarkResponse temp = toMarkResponse(mark);
            responseList.add(temp);
        }

        return responseList;
    }

    @Override
    public MarkResponse getMark(String markId) {
        MarkEntity markEntity = markRepository.findById(markId).orElseThrow(
                () -> new AppException(ErrorCode.MARK_NOT_FOUND)
        );

        return toMarkResponse(markEntity);
    }

    @Override
    public List<MarkResponse> getMarkByStudent(String studentId) {

        //check studentId
        if (!studentRepository.existsById(studentId)) {
            throw new AppException(ErrorCode.STUDENT_NOT_FOUND);
        }

        List<MarkEntity> markEntityList = markRepository.findByStudentEntityStudentId(studentId);
        if (!markEntityList.isEmpty()) {
            return toMarkResponseList(markEntityList);
        } else {
            throw new AppException(ErrorCode.MARK_LIST_EMPTY);
        }
    }

    @Override
    public void countAverageMark(String studentId) {

    }

    @Override
    public MarkResponse updateMark(String markId, MarkUpdateRequest request) {
        MarkEntity markEntity = markRepository.findById(markId).orElseThrow(
                () -> new AppException(ErrorCode.MARK_NOT_FOUND)
        );

        //convert
        markEntity.setMark(request.getMark());
        markEntity.setStatus(request.isStatus());
        markEntity.setComment(request.getComment());

        markRepository.save(markEntity);

        return getMark(markId);
    }

    @Override
    public void deleteMark(String markId) {
        markRepository.deleteById(markId);
    }
}
