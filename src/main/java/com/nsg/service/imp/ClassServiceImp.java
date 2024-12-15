package com.nsg.service.imp;

import com.nsg.Mapper.ClassMapper;
import com.nsg.Mapper.ClassMapperImpl;
import com.nsg.common.exception.AppException;
import com.nsg.common.exception.ErrorCode;
import com.nsg.dto.request.classRequest.ClassRequest;
import com.nsg.dto.response.classResponse.ClassResponse;
import com.nsg.entity.BatchEntity;
import com.nsg.entity.ClassEntity;
import com.nsg.entity.SessionEntity;
import com.nsg.entity.StudentEntity;
import com.nsg.repository.*;
import com.nsg.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClassServiceImp implements ClassService {

    @Autowired
    ClassRepository classRepository;

    @Autowired
    BatchRepository batchRepository;
    @Autowired
    SessionRepository sessionRepository;
    @Autowired
    private ClassMapperImpl classMapperImpl;

    @Autowired
    AttendanceRepository attendanceRepository;

    @Autowired
    MarkRepository markRepository;

    @Autowired
    StudentRepository studentRepository;

    @Override
    public void createClass(ClassRequest request) {

        //check batch status
        BatchEntity batch = batchRepository.findByBatchName(request.getBatchName()).orElseThrow(
                () -> new AppException(ErrorCode.BATCH_NOT_EXISTED)
        );
        if (batch.getBatchStatus() == 2) {
            //check class name existed?
            if (!classRepository.findByClassNameAndBatchEntityBatchName(request.getClassName(), request.getBatchName()).isEmpty()) {
                throw new AppException(ErrorCode.CLASS_NAME_EXISTED);
            } else {
                //create new class
                ClassEntity classEntity = new ClassEntity();
                classEntity.setClassName(request.getClassName());
                classEntity.setClassColour(request.getClassColour());

                //set default class status is true
                classEntity.setClassStatus( true );

                classEntity.setBatchEntity(batch);
                batch.getClassEntityList().add(classEntity);
                batchRepository.save(batch);
                classRepository.save(classEntity);
            }
        } else {
            throw new AppException(ErrorCode.BATCH_IS_CLOSED);
        }

    }

    @Override
    public ClassResponse getClass(String classId) {
        ClassEntity classEntity = classRepository.findById(classId).orElseThrow(
                () -> new AppException(ErrorCode.CLASS_NOT_FOUND)
        );

        ClassResponse response = toClassResponse(classEntity);

        return response;
    }

    @Override
    public ClassResponse toClassResponse(ClassEntity classEntity) {
        ClassResponse response = new ClassResponse();
        response.setClassId(classEntity.getClassId());
        response.setClassName(classEntity.getClassName());
        response.setClassColour(classEntity.getClassColour());
        response.setTotalStudent( classEntity.getStudentEntityList().size() );
        response.setClassStatus(classEntity.isClassStatus() );

        return response;
    }

    @Override
    public Page<ClassResponse> getAllClass(int page, int size) {
        Page<ClassEntity> classEntityList = classRepository.findAll(PageRequest.of(page, size));

        List<ClassResponse> responseList = toClassResponseList(classEntityList.getContent());

        return new PageImpl<>(responseList, classEntityList.getPageable(), classEntityList.getTotalElements());
    }

    @Override
    public Page<ClassResponse> getClassByBatch(String batchName, int page, int size) {
        if (!batchRepository.existsByBatchName(batchName)) {
            throw new AppException(ErrorCode.BATCH_NOT_EXISTED);
        }

        Page<ClassEntity> classEntitiesList = classRepository.findByBatchEntityBatchName(batchName, PageRequest.of(page, size));

        List<ClassResponse> responseList = toClassResponseList(classEntitiesList.getContent());

        return new PageImpl<>(responseList, classEntitiesList.getPageable(), classEntitiesList.getTotalElements());
    }

    public List<ClassResponse> toClassResponseList(List<ClassEntity> classEntityList) {
        List<ClassResponse> responseList = new ArrayList<>();

        for (ClassEntity classEntity : classEntityList) {
            ClassResponse tempResponse = toClassResponse(classEntity);

            //get number student in class
//            int totalStudent = classEntity.getStudentEntityList().size();
//
//            tempResponse.setTotalStudent(totalStudent);

            responseList.add(tempResponse);
        }

        return responseList;
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


    @Override
    public List<ClassResponse> getClassByTeacherId(String teacherId) {
        List<ClassResponse> classEntities = new ArrayList<>();

        List<SessionEntity> sessionEntityList = sessionRepository.findByUserId(teacherId);

        for (SessionEntity session : sessionEntityList) {
            boolean exists = false;

            // Kiểm tra danh sách classEntities
            for (ClassResponse classResponse : classEntities) {
                if (classResponse.getClassId().equals(session.getClassEntity().getClassId())) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                ClassResponse classResponse = ClassMapper.INSTANCE.toClassResponse(session.getClassEntity());
                classResponse.setTotalStudent(session.getClassEntity().getStudentEntityList().size());
                classResponse.setBatchName( session.getClassEntity().getBatchEntity().getBatchName() );
                classEntities.add( classResponse );
            }
        }
        return classEntities;
    }

    //change class status
    @Override
    public void changeClassStatus(String classId) {
        ClassEntity classEntity = classRepository.findById(classId).orElseThrow(
                () -> new AppException(ErrorCode.CLASS_NOT_FOUND)
        );

        boolean oldStatus = classEntity.isClassStatus();

        classEntity.setClassStatus(!oldStatus);

        classRepository.save(classEntity);
    }

    //delete class
    @Override
    @Transactional
    public void deleteClassAndRelatedEntities(String classId) {
        // Xóa attendance
        attendanceRepository.deleteByClassId(classId);

        // Xóa session
        sessionRepository.deleteByClassId(classId);

        // Xóa mark
        markRepository.deleteMarksByClassId(classId);

        // Xóa student
        studentRepository.deleteByClassId(classId);

        // Xóa class
        classRepository.deleteByClassId(classId);
    }

}
