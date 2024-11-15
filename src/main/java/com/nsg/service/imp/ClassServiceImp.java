package com.nsg.service.imp;

import com.nsg.common.exception.AppException;
import com.nsg.common.exception.ErrorCode;
import com.nsg.dto.request.classRequest.ClassRequest;
import com.nsg.dto.response.classResponse.ClassResponse;
import com.nsg.entity.ClassEntity;
import com.nsg.repository.ClassRepository;
import com.nsg.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClassServiceImp implements ClassService {

    @Autowired
    ClassRepository classRepository;

    @Override
    public void createClass(ClassRequest request) {

        //check class name existed?
        if (classRepository.findByClassName(request.getClassName()) != null){
            throw new AppException(ErrorCode.CLASS_NAME_EXISTED);
        }else {

            //create new class
            ClassEntity classEntity = new ClassEntity();
            classEntity.setClassName(request.getClassName());
            classEntity.setClassColour(request.getClassColour());

            classRepository.save(classEntity);
        }
    }

    @Override
    public ClassResponse getClass(String classId) {
        ClassEntity classEntity = classRepository.findById(classId).orElseThrow(
                () -> new AppException(ErrorCode.CLASS_NOT_FOUND)
        );

        ClassResponse response = new ClassResponse();
        response.setClassId(classEntity.getClassId());
        response.setClassName(classEntity.getClassName());
        response.setClassColour(classEntity.getClassColour());

        return response;
    }

    @Override
    public Page<ClassResponse> getAllClass(int page, int size) {
        Page<ClassEntity> classEntityList = classRepository.findAll(PageRequest.of(page, size));

        List<ClassResponse> responseList = new ArrayList<>();

        for (ClassEntity classEntity : classEntityList) {
            ClassResponse tempResponse = new ClassResponse();
            tempResponse.setClassId(classEntity.getClassId());
            tempResponse.setClassName(classEntity.getClassName());
            tempResponse.setClassColour(classEntity.getClassColour());

            responseList.add(tempResponse);
        }

        return new PageImpl<>(responseList, classEntityList.getPageable(), classEntityList.getTotalElements());
    }

    @Override
    public ClassResponse updateClass(String classId, ClassRequest request) {
        ClassEntity classEntity = classRepository.findById(classId).orElseThrow(
                () -> new AppException(ErrorCode.CLASS_NOT_FOUND)
        );

        classEntity.setClassName(request.getClassName());
        classEntity.setClassColour(request.getClassColour());

        classRepository.save(classEntity);

        return getClass(classId);
    }

    @Override
    public void deleteClass(String classId) {
        classRepository.deleteById(classId);
    }
}
