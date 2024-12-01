package com.nsg.service;

import com.nsg.dto.request.classRequest.ClassRequest;
import com.nsg.dto.response.classResponse.ClassResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ClassService {

    //create a class
    void createClass(ClassRequest request);

    //get a class
    ClassResponse getClass(String classId);

    //get all class
    Page<ClassResponse> getAllClass(int page, int size);

    //get all class by batch
    Page<ClassResponse> getClassByBatch(String batchName, int page, int size);

    //update a class
    ClassResponse updateClass(String classId, ClassRequest request);

    //delete a class
    void deleteClass(String classId);

    List<ClassResponse> getClassByTeacherId(String teacherId);
}
