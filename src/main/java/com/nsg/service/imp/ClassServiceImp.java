package com.nsg.service.imp;

import com.nsg.dto.request.classRequest.ClassRequest;
import com.nsg.dto.response.classResponse.ClassResponse;
import com.nsg.repository.ClassRepository;
import com.nsg.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassServiceImp implements ClassService {

    @Autowired
    ClassRepository classRepository;

    @Override
    public void createClass(ClassRequest request) {

    }

    @Override
    public ClassResponse getClass(String classId) {
        return null;
    }

    @Override
    public List<ClassResponse> getAllClass() {
        return null;
    }

    @Override
    public ClassResponse updateClass(String classId, ClassRequest request) {
        return null;
    }

    @Override
    public void deleteClass(String classId) {

    }
}
