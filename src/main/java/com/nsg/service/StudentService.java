package com.nsg.service;

import com.nsg.dto.request.student.StudentCreattionRequest;
import com.nsg.dto.response.student.StudentResponse;
import com.nsg.entity.StudentEntity;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StudentService {

    //create student
    StudentResponse createStudent(StudentCreattionRequest request);

    //get all student
    Page<StudentResponse> getAllStudent(int page, int size);

    //get student by id
    StudentResponse getStudent(String studentId);

    //get student by batchName
    Page<StudentResponse> getStudentByBatchName(int page, int size, String batchName);

    //get student by batchName and className
    Page<StudentResponse> getStudentByBatchNameAndClassName(int page, int size, String batchName, String className);

    void saveAll(List<StudentEntity> students);

    Page<StudentResponse> findStudentsByName(String name, int page, int size);
}

