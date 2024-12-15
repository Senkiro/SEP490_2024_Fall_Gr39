package com.nsg.service;

import com.nsg.dto.request.mark.MarkCreationRequest;
import com.nsg.dto.request.mark.MarkUpdateRequest;
import com.nsg.dto.response.mark.MarkResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface MarkService {

    //create new mark for student, exam
    void createMark(MarkCreationRequest request);

    //get all mark
    Page<MarkResponse> getAllMark(int page, int size);

    //get a mark by id
    MarkResponse getMark(String markId);

    //get mark by studentId
    List<MarkResponse> getMarkByStudent(String studentId);

    //count average mark for student
    void calculateAverageMark(String studentId, double participationMark);

    //update mark
    MarkResponse updateMark(String markId, MarkUpdateRequest request);

    //update many marks at the same request
    List<MarkResponse> updateMarks(List<MarkUpdateRequest> requests);

    //delete mark
    void deleteMark(String markId);

    void generateMarkForAllStudentInClass(String classId);

    List<MarkResponse> getMarkByExamAndSessionClass(int examId, String classId);

    void calculateAllStudentsMarkInClass(String classId);

    void createAllMarkForOneStudent(String studentId);

    void courseSummary(String classId);

    int calculateTotalMarkedInDay(LocalDate date);
}
