package com.nsg.service;

import com.nsg.dto.request.student.StudentCreattionRequest;
import com.nsg.dto.response.staff.StudentResponse;
import com.nsg.entity.StudentEntity;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface StudentService {

    //create student
    StudentEntity createStudent(StudentCreattionRequest request, String batch_name);

    //get all student
    Page<StudentResponse> getAllStudent(int page, int size);

    //get student by batchName
    Page<StudentResponse> getStudentByBatchName(int page, int size, String batchName);
}
