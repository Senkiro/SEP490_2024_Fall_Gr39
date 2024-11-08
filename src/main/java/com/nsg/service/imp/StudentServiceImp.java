package com.nsg.service.imp;

import com.nsg.Mapper.UserMapper;
import com.nsg.common.enums.UserRole;
import com.nsg.common.exception.AppException;
import com.nsg.common.exception.ErrorCode;
import com.nsg.dto.request.student.StudentCreattionRequest;
import com.nsg.dto.request.user.UserCreationRequest;
import com.nsg.dto.response.staff.StudentResponse;
import com.nsg.dto.response.user.UserInforResponse;
import com.nsg.entity.BatchEntity;
import com.nsg.entity.StudentEntity;
import com.nsg.entity.UserEntity;
import com.nsg.repository.BatchRepository;
import com.nsg.repository.StudentRepository;
import com.nsg.service.StudentService;
import com.nsg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class StudentServiceImp implements StudentService {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    BatchRepository batchRepository;

    @Autowired
    UserService userService;

    @Override
    public StudentEntity createStudent(StudentCreattionRequest request, String batch_name){
        StudentEntity student = new StudentEntity();
        student.setRollNumber(generateRollNumber());

        //map
        UserCreationRequest userCreationRequest = request;
        //create user
        UserEntity user = userService.createUser(userCreationRequest, UserRole.STUDENT);

        //set user
        student.setUser(user);

        //get batch by batchName
        BatchEntity batch = batchRepository.findByBatchName(batch_name).orElseThrow(
                () -> new AppException(ErrorCode.BATCH_NOT_EXISTED)
        );

        //set batch
        student.setBatchEntity(batch);

        batch.getStudentEntityList().add(student);

        batchRepository.save(batch);

        return studentRepository.save(student);
    };

    @Override
    public Page<StudentResponse> getAllStudent(int page, int size){

        //find all student
        Page<StudentEntity> studentEntityList = studentRepository.findAll(PageRequest.of(page, size));

        //generate list for response
        List<StudentResponse> studentListResponse = new ArrayList<>();

        //for
        for (StudentEntity student : studentEntityList) {
            StudentResponse studentResponse = new StudentResponse();
            //get,set rollNumber
            studentResponse.setRollNumber(student.getRollNumber());

            //map user to UserInforResponse
            UserInforResponse userInforResponse = UserMapper.INSTANCE.toUserInforResponse(student.getUser());
            //set user
            studentResponse.setUser(userInforResponse);

            //add to response list
            studentListResponse.add(studentResponse);
        }

        return new PageImpl<>(studentListResponse, studentEntityList.getPageable(), studentEntityList.getTotalElements());
    }

    @Override
    public Page<StudentResponse> getStudentByBatchName(int page, int size, String batchName) {

        //check batch existed
        if (batchRepository.findByBatchName(batchName).isEmpty()) {
            throw new AppException(ErrorCode.BATCH_NOT_EXISTED);
        }

        //find all student
        Page<StudentEntity> studentEntityList = studentRepository.findByBatchEntityBatchName(batchName, PageRequest.of(page, size));

        //generate list for response
        List<StudentResponse> studentListResponse = new ArrayList<>();

        //for
        for (StudentEntity student : studentEntityList) {
            StudentResponse studentResponse = new StudentResponse();
            //get,set rollNumber
            studentResponse.setRollNumber(student.getRollNumber());

            //map user to UserInforResponse
            UserInforResponse userInforResponse = UserMapper.INSTANCE.toUserInforResponse(student.getUser());
            //set user
            studentResponse.setUser(userInforResponse);

            //add to response list
            studentListResponse.add(studentResponse);
        }

        return new PageImpl<>(studentListResponse, studentEntityList.getPageable(), studentEntityList.getTotalElements());
    }

    //get student by batchName


    //generate random roll number
    public String generateRollNumber(){
        String prefix = "FA";
        int year = LocalDate.now().getYear() % 100;

        Random random = new Random();
        int randomNumber = 1000 + random.nextInt(9000);

        return prefix+year+String.format("%04d", randomNumber);
    }
}
