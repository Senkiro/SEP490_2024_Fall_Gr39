package com.nsg.service.imp;

import com.nsg.Mapper.ClassMapper;
import com.nsg.Mapper.UserMapper;
import com.nsg.common.enums.UserRole;
import com.nsg.common.exception.AppException;
import com.nsg.common.exception.ErrorCode;
import com.nsg.dto.request.student.StudentCreattionRequest;
import com.nsg.dto.request.user.UserCreationRequest;
import com.nsg.dto.response.attendance.AttendanceStatisticsResponse;
import com.nsg.dto.response.batch.BatchResponse;
import com.nsg.dto.response.student.StudentResponse;
import com.nsg.dto.response.user.UserInforResponse;
import com.nsg.entity.BatchEntity;
import com.nsg.entity.ClassEntity;
import com.nsg.entity.StudentEntity;
import com.nsg.entity.UserEntity;
import com.nsg.repository.*;
import com.nsg.service.AttendanceService;
import com.nsg.service.MarkService;
import com.nsg.service.StudentService;
import com.nsg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    ClassRepository classRepository;

    @Autowired
    AttendanceRepository attendanceRepository;

    @Override
    public StudentResponse createStudent(StudentCreattionRequest request) {
        StudentEntity student = new StudentEntity();
        student.setRollNumber(generateRollNumber());

        UserCreationRequest userCreationRequest = request;
        UserEntity user = userService.createUser(userCreationRequest, UserRole.STUDENT);
        student.setUser(user);

        BatchEntity batch = batchRepository.findByBatchName(request.getBatchName())
                .orElseThrow(() -> new AppException(ErrorCode.BATCH_NOT_EXISTED));
        student.setBatchEntity(batch);

        List<ClassEntity> classes = classRepository.findByClassNameAndBatchEntityBatchName(
                request.getClassName(), request.getBatchName());

        if (classes.size() > 1) {
            throw new AppException(ErrorCode.CLASS_NOT_FOUND);
        } else if (classes.isEmpty()) {
            throw new AppException(ErrorCode.CLASS_NOT_FOUND);
        }
        ClassEntity classEntity = classes.get(0);
        student.setClassEntity(classEntity);

        batch.getStudentEntityList().add(student);
        classEntity.getStudentEntityList().add(student);

        studentRepository.save(student);
        batchRepository.save(batch);
        classRepository.save(classEntity);

        return null;
    }

    @Override
    public Page<StudentResponse> getAllStudent(int page, int size){

        //find all student
        Page<StudentEntity> studentEntityList = studentRepository.findAll(PageRequest.of(page, size));

        //generate list for response
        List<StudentResponse> studentListResponse = toStudentResponseList(studentEntityList.getContent());

        return new PageImpl<>(studentListResponse, studentEntityList.getPageable(), studentEntityList.getTotalElements());
    }

    @Override
    public StudentResponse getStudent(String studentId) {
        StudentEntity studentEntity = studentRepository.findById(studentId).orElseThrow(
                () -> new AppException(ErrorCode.STUDENT_NOT_FOUND)
        );

        return convertToStudentResponse(studentEntity);
    }

    //get student by batchName
    @Override
    public Page<StudentResponse> getStudentByBatchName(int page, int size, String batchName) {

        //check batch existed
        if (batchRepository.findByBatchName(batchName).isEmpty()) {
            throw new AppException(ErrorCode.BATCH_NOT_EXISTED);
        }

        //find all student
        Page<StudentEntity> studentEntityList = studentRepository.findByBatchEntityBatchName(batchName, PageRequest.of(page, size));

        //generate list for response
        List<StudentResponse> studentListResponse = toStudentResponseList(studentEntityList.getContent());

        return new PageImpl<>(studentListResponse, studentEntityList.getPageable(), studentEntityList.getTotalElements());
    }

    @Override
    public Page<StudentResponse> getStudentByBatchNameAndClassName(int page, int size,
                                                                   String batchName,
                                                                   String classId) {
        //check batch existed
        if (batchRepository.findByBatchName(batchName).isEmpty()) {
            throw new AppException(ErrorCode.BATCH_NOT_EXISTED);
        }

        //check class existed
        if (classRepository.findByClassId(classId) == null) {
            throw new AppException(ErrorCode.CLASS_NOT_FOUND);
        }

        //find all student
        Page<StudentEntity> studentEntityList =
                studentRepository.findByBatchEntityBatchNameAndClassEntityClassId(
                        batchName,
                        classId,
                        PageRequest.of(page, size));

        //generate list for response
        List<StudentResponse> studentListResponse = new ArrayList<>();

        //for
        for (StudentEntity student : studentEntityList) {
            StudentResponse studentResponse = new StudentResponse();
            //get,set rollNumber
            studentResponse.setRollNumber(student.getRollNumber());
            studentResponse.setStudentId(student.getStudentId());

            //map user to UserInforResponse
            UserInforResponse userInforResponse =
                    UserMapper.INSTANCE.toUserInforResponse(student.getUser());
            //set user
            studentResponse.setUserInforResponse(userInforResponse);

            //set batch
            studentResponse.setBatchName(student.getBatchEntity().getBatchName());

            //set class
            studentResponse.setClassResponse(ClassMapper.INSTANCE.toClassResponse(student.getClassEntity()));

            //add to response list
            studentListResponse.add(studentResponse);
        }

        return new PageImpl<>(studentListResponse,
                studentEntityList.getPageable(),
                studentEntityList.getTotalElements());
    }

    @Override
    public StudentResponse convertToStudentResponse(StudentEntity studentEntity) {
        StudentResponse studentResponse = new StudentResponse();

        studentResponse.setRollNumber(studentEntity.getRollNumber());
        studentResponse.setStudentId(studentEntity.getStudentId());

        //map user to UserInforResponse
        UserInforResponse userInforResponse =
                UserMapper.INSTANCE.toUserInforResponse(studentEntity.getUser());
        //set user
        studentResponse.setUserInforResponse(userInforResponse);

        //set batch
        studentResponse.setBatchName(studentEntity.getBatchEntity().getBatchName());

        //set class
        studentResponse.setClassResponse( ClassMapper.INSTANCE.toClassResponse(studentEntity.getClassEntity()) );

        //set avg mark
        studentResponse.setAvgMark( studentEntity.getAvgMark() );

        studentResponse.setAvgMark( studentEntity.getAvgMark() );

        return studentResponse;
    }


    //generate random roll number
    public String generateRollNumber(){
        String prefix = "FA";
        int year = LocalDate.now().getYear() % 100;

        Random random = new Random();
        int randomNumber = 1000 + random.nextInt(9000);

        return prefix+year+String.format("%04d", randomNumber);
    }

    public void saveAll(List<StudentEntity> students) {
        studentRepository.saveAll(students);
    }

    @Override
    public Page<StudentResponse> findStudentsByName(String name, String classId, int page, int size) {
        //find all student
        Page<UserEntity> userEntityList = userRepository.findByFullNameContaining(name,PageRequest.of(page, size));

        //generate list for response
        List<StudentResponse> studentListResponse = new ArrayList<>();

        //for
        for (UserEntity user : userEntityList) {
            // check classId
            if (user.getStudentEntity().getClassEntity().getClassId().equals(classId)) {
                StudentResponse studentResponse = new StudentResponse();
                //get,set rollNumber
                studentResponse.setRollNumber(user.getStudentEntity().getRollNumber());
                studentResponse.setStudentId(user.getStudentEntity().getStudentId());

                //map user to UserInforResponse
                UserInforResponse userInforResponse = UserMapper.INSTANCE.toUserInforResponse(user);
                //set user
                studentResponse.setUserInforResponse(userInforResponse);

                //set batch
                studentResponse.setBatchName(user.getStudentEntity().getBatchEntity().getBatchName());

                //set class
                studentResponse.setClassResponse(ClassMapper.INSTANCE.toClassResponse(user.getStudentEntity().getClassEntity()));

                //set mark
                studentResponse.setAvgMark( user.getStudentEntity().getAvgMark() );

                //add to response list
                studentListResponse.add(studentResponse);
            }

        }

        return new PageImpl<>(studentListResponse, userEntityList.getPageable(), userEntityList.getTotalElements());
    }

    @Override
    public Page<StudentResponse> findStudentsByNameByBatch(String name, String batch_name, int page, int size) {
        //find all student
        Page<UserEntity> userEntityList = userRepository.findByFullNameContaining(name,PageRequest.of(page, size));

        //generate list for response
        List<StudentResponse> studentListResponse = new ArrayList<>();

        //for
        for (UserEntity user : userEntityList) {
            // check classId
            if (user.getStudentEntity().getBatchEntity().getBatchName().equals(batch_name)) {
                StudentResponse studentResponse = new StudentResponse();
                //get,set rollNumber
                studentResponse.setRollNumber(user.getStudentEntity().getRollNumber());
                studentResponse.setStudentId(user.getStudentEntity().getStudentId());

                //map user to UserInforResponse
                UserInforResponse userInforResponse = UserMapper.INSTANCE.toUserInforResponse(user);
                //set user
                studentResponse.setUserInforResponse(userInforResponse);

                //set batch
                studentResponse.setBatchName(user.getStudentEntity().getBatchEntity().getBatchName());

                //set class
                studentResponse.setClassResponse(ClassMapper.INSTANCE.toClassResponse(user.getStudentEntity().getClassEntity()));

                //set mark
                studentResponse.setAvgMark( user.getStudentEntity().getAvgMark() );

                //add to response list
                studentListResponse.add(studentResponse);
            }

        }

        return new PageImpl<>(studentListResponse, userEntityList.getPageable(), userEntityList.getTotalElements());
    }

    @Override
    public Page<StudentResponse> getStudentByClass(int page, int size, String studentId) {
        StudentEntity currentStudent = studentRepository.findById(studentId)
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));

        String classId = currentStudent.getClassEntity().getClassId();

        ClassEntity classEntity = classRepository.findByClassId(classId);
        if (classEntity == null) {
            throw new AppException(ErrorCode.CLASS_NOT_FOUND);
        }
        Page<StudentEntity> studentEntityList = studentRepository.findByClassEntityClassId(classId,PageRequest.of(page, size));

        List<StudentResponse> studentListResponse = toStudentResponseList(studentEntityList.getContent());

        return new PageImpl<>(studentListResponse, studentEntityList.getPageable(), studentEntityList.getTotalElements());

    }

    @Override
    public Page<StudentResponse> getStudentByClassId(int page, int size, String classId) {
        //check class existed
        if (classRepository.findByClassId(classId) == null) {
            throw new AppException(ErrorCode.CLASS_NOT_FOUND);
        }

        //find all student
        Page<StudentEntity> studentEntityList =
                studentRepository.findByClassEntityClassId(
                        classId,
                        PageRequest.of(page, size));

        //generate list for response
        List<StudentResponse> studentListResponse = toStudentResponseListWithAttendanceStatistic(studentEntityList.getContent());

        return new PageImpl<>(studentListResponse,
                studentEntityList.getPageable(),
                studentEntityList.getTotalElements());
    }

    @Override
    public void markUpdate(String studentId, float avgMark) {
        StudentEntity student = studentRepository.findById(studentId).orElseThrow(
                () -> new AppException(ErrorCode.STUDENT_NOT_FOUND)
        );

        student.setAvgMark(avgMark);

        studentRepository.save(student);

    }

    //convert data to list of student response
    public List<StudentResponse> toStudentResponseList(List<StudentEntity> studentEntityList) {
        List<StudentResponse> studentListResponse = new ArrayList<>();

        for (StudentEntity student : studentEntityList) {
            StudentResponse studentResponse = new StudentResponse();
            //get,set rollNumber
            studentResponse.setRollNumber(student.getRollNumber());
            studentResponse.setStudentId(student.getStudentId());

            //map user to UserInforResponse
            UserInforResponse userInforResponse = UserMapper.INSTANCE.toUserInforResponse(student.getUser());
            //set user
            studentResponse.setUserInforResponse(userInforResponse);

            //set batch
            studentResponse.setBatchName(student.getBatchEntity().getBatchName());

            //set class
            studentResponse.setClassResponse(ClassMapper.INSTANCE.toClassResponse(student.getClassEntity()));

            studentResponse.setAvgMark( student.getAvgMark() );

            //add to response list
            studentListResponse.add(studentResponse);
        }
        return studentListResponse;
    }

    //convert data to list of student response BUT: with attendance statistics information
    public List<StudentResponse> toStudentResponseListWithAttendanceStatistic(List<StudentEntity> studentEntityList) {
        List<StudentResponse> studentListResponse = new ArrayList<>();

        for (StudentEntity student : studentEntityList) {
            StudentResponse studentResponse = new StudentResponse();
            //get,set rollNumber
            studentResponse.setRollNumber(student.getRollNumber());
            studentResponse.setStudentId(student.getStudentId());

            //map user to UserInforResponse
            UserInforResponse userInforResponse = UserMapper.INSTANCE.toUserInforResponse(student.getUser());
            //set user
            studentResponse.setUserInforResponse(userInforResponse);

            //set batch
            studentResponse.setBatchName(student.getBatchEntity().getBatchName());

            //set class
            studentResponse.setClassResponse(ClassMapper.INSTANCE.toClassResponse(student.getClassEntity()));

            //get attendance statistics
            AttendanceStatisticsResponse attendanceStatisticsResponse = getDataAttendanceStatisticsResponse(student.getStudentId());

            studentResponse.setAttendanceStatisticsResponse(attendanceStatisticsResponse);

            //set avg mark
            studentResponse.setAvgMark( student.getAvgMark() );

            //add to response list
            studentListResponse.add(studentResponse);
        }
        return studentListResponse;
    }

    public AttendanceStatisticsResponse getDataAttendanceStatisticsResponse(String studentId) {
        List<Object[]> result = attendanceRepository.getAttendanceStatistics(studentId);

        AttendanceStatisticsResponse attendanceStatisticsResponse = new AttendanceStatisticsResponse();

        if (result.isEmpty()) {
            throw new AppException(ErrorCode.NO_DATA_ATTENDANCE);
        }

        Object[] row = result.get(0);
        long attendCount = ((Number) row[0]).longValue();
        long totalCount = ((Number) row[1]).longValue();
        double attendPercentage = ((Number) row[2]).doubleValue();

        attendanceStatisticsResponse.setAttendCount(attendCount);
        attendanceStatisticsResponse.setTotalCount(totalCount);
        attendanceStatisticsResponse.setAttendPercentage(attendPercentage);
        return attendanceStatisticsResponse;
    }

    //get top 10 student by avg mark in a batch
    @Override
    public List<StudentResponse> getTop10Students(String batchName) {
        Pageable pageable = PageRequest.of(0, 10); // Page 0, size 10
        List<StudentEntity> studentEntityList = studentRepository.findTop10ByBatchOrderByAvgMarkDesc(batchName, pageable);
        return toStudentResponseList(studentEntityList);
    }

    //change student class
    @Override
    public void changeClassForStudent(String studentId, String classId) {
        StudentEntity student = studentRepository.findById(studentId).orElseThrow(
                () -> new AppException(ErrorCode.STUDENT_NOT_FOUND)
        );

        //get class
        ClassEntity newClass = classRepository.findByClassId(classId);

        //check batch status
        BatchEntity batch = student.getBatchEntity();

        // if status = 2 -> then can change student in batch
        if (batch.getBatchStatus() == 2) {

            //change student class
            student.setClassEntity(newClass);

            //save
            studentRepository.save(student);
        }
    }



}
