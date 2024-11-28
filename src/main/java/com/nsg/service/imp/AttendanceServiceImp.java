package com.nsg.service.imp;

import com.nsg.common.exception.AppException;
import com.nsg.common.exception.ErrorCode;
import com.nsg.dto.request.attendance.AttendanceRequest;
import com.nsg.dto.response.attendance.AttendanceResponse;
import com.nsg.dto.response.batch.BatchResponse;
import com.nsg.dto.response.student.StudentResponse;
import com.nsg.entity.AttendanceEntity;
import com.nsg.entity.BatchEntity;
import com.nsg.entity.SessionEntity;
import com.nsg.entity.StudentEntity;
import com.nsg.repository.AttendanceRepository;
import com.nsg.repository.ClassRepository;
import com.nsg.repository.SessionRepository;
import com.nsg.repository.StudentRepository;
import com.nsg.service.AttendanceService;
import com.nsg.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AttendanceServiceImp implements AttendanceService {

    @Autowired
    AttendanceRepository attendanceRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    StudentService studentService;

    @Autowired
    ClassRepository classRepository;

    @Override
    public void createAttendance(AttendanceRequest request) {
        AttendanceEntity attendanceEntity = new AttendanceEntity();
        attendanceEntity.setStatus(request.getStatus());
        attendanceEntity.setNote(request.getNote());

        //set session, student
        //get student by id
        StudentEntity student = studentRepository.findById(request.getStudentId()).orElseThrow(
                () -> new AppException(ErrorCode.STUDENT_NOT_FOUND)
        );
        attendanceEntity.setStudentEntity(student);

        //get session
        SessionEntity session = sessionRepository.findById(request.getSessionId()).orElseThrow(
                () -> new AppException(ErrorCode.SESSION_NOT_FOUND)
        );
        attendanceEntity.setSessionEntity(session);

        attendanceRepository.save(attendanceEntity);
    }

    //create attendance for all student in class of one session
    @Override
    public void createAttendancesForSession(String classId) {
        //check class existed or not
        if (!classRepository.existsById(classId)) {
            throw new AppException(ErrorCode.CLASS_NOT_FOUND);
        }

        // get session by classId
        List<SessionEntity> sessionEntityList = sessionRepository.findByClassEntityClassId(classId);

        //check session
        if (sessionEntityList.isEmpty()) {
            //There are no session for this class
            throw new AppException(ErrorCode.SESSION_LIST_EMPTY);
        }

        // filter which session has CurriculumnEntity not null
        List<SessionEntity> validSessions = new ArrayList<>();
        for (SessionEntity session : sessionEntityList) {
            if (session.getCurriculumnEntity() != null) {
                validSessions.add(session);
            }
        }

        // get all student in class
        List<StudentEntity> studentEntityList = studentRepository.findByClassEntityClassId(classId);

        //if there are no student in class
        if (studentEntityList.isEmpty()) {
            throw new AppException(ErrorCode.CLASS_IS_EMPTY);
        }

        // list AttendanceRequest
        List<AttendanceRequest> createRequest = new ArrayList<>();
        String defaultStatus = "incoming";

        for (SessionEntity session : validSessions) {
            for (StudentEntity student : studentEntityList) {
                AttendanceRequest attendanceRequest = new AttendanceRequest();

                attendanceRequest.setStatus(defaultStatus);
                attendanceRequest.setSessionId(session.getSessionId());
                attendanceRequest.setStudentId(student.getStudentId());

                createRequest.add(attendanceRequest);
            }
        }

        // call create attendance function
        for (AttendanceRequest request : createRequest) {
            createAttendance(request);
        }
    }


    @Override
    public Page<AttendanceResponse> getAllAttendance(int page, int size) {
        Page<AttendanceEntity> attendanceEntities = attendanceRepository.findAll(PageRequest.of(page, size));
        List<AttendanceResponse> responseList = toListAttendanceResponse(attendanceEntities);

        return new PageImpl<>(responseList, attendanceEntities.getPageable(), attendanceEntities.getTotalElements());
    }

    @Override
    public Page<AttendanceResponse> getAttendanceBySession(String sessionId, int page, int size) {
        Page<AttendanceEntity> attendanceEntities = attendanceRepository.findBySessionEntitySessionId(sessionId, PageRequest.of(page, size));
        List<AttendanceResponse> responseList = toListAttendanceResponse(attendanceEntities);

        return new PageImpl<>(responseList, attendanceEntities.getPageable(), attendanceEntities.getTotalElements());
    }

    @Override
    public Page<AttendanceResponse> getAttendanceByStudent(String studentId, int page, int size) {
        Page<AttendanceEntity> attendanceEntities = attendanceRepository.findByStudentEntityStudentId(studentId, PageRequest.of(page, size));
        List<AttendanceResponse> responseList = toListAttendanceResponse(attendanceEntities);

        return new PageImpl<>(responseList, attendanceEntities.getPageable(), attendanceEntities.getTotalElements());
    }

    //convert from page to list response
    public List<AttendanceResponse> toListAttendanceResponse(Page<AttendanceEntity> attendanceEntities) {
        List<AttendanceResponse> responseList = new ArrayList<>();

        for (AttendanceEntity attendance : attendanceEntities) {
            AttendanceResponse attendanceResponse = new AttendanceResponse();

            attendanceResponse.setAttendanceId(attendance.getAttendanceId());
            attendanceResponse.setDate(attendance.getSessionEntity().getDate());
            attendanceResponse.setStatus(attendance.getStatus());
            attendanceResponse.setNote(attendance.getNote());

            attendanceResponse.setSessionId(attendance.getSessionEntity().getSessionId());

            StudentEntity student = attendance.getStudentEntity();

            //convert to student response
            StudentResponse studentResponse = studentService.convertToStudentResponse(student);

            //get teacher in session
            SessionEntity session = attendance.getSessionEntity();

            if (session.getUser() != null) {
                attendanceResponse.setTeacher(session.getUser().getUsername());
            }

            attendanceResponse.setStudentResponse(studentResponse);

            responseList.add(attendanceResponse);
        }

        return responseList;
    }

    @Override
    public AttendanceResponse getAttendance(String attendanceId) {
        AttendanceEntity attendanceEntity = attendanceRepository.findById(attendanceId).orElseThrow(
                () -> new AppException(ErrorCode.ATTENDANCE_NOT_FOUND)
        );

        AttendanceResponse attendanceResponse = new AttendanceResponse();

        attendanceResponse.setAttendanceId(attendanceEntity.getAttendanceId());
        attendanceResponse.setDate(attendanceEntity.getSessionEntity().getDate());
        attendanceResponse.setStatus(attendanceEntity.getStatus());
        attendanceResponse.setNote(attendanceEntity.getNote());

        attendanceResponse.setSessionId(attendanceEntity.getSessionEntity().getSessionId());

        StudentEntity student = attendanceEntity.getStudentEntity();

        if (student != null) {
            StudentResponse studentResponse = studentService.convertToStudentResponse(student);
            attendanceResponse.setStudentResponse(studentResponse);
        }
        else
            attendanceResponse.setStudentResponse(null);

        return attendanceResponse;
    }

    @Override
    public AttendanceResponse updateAttendance(String attendanceId, AttendanceRequest request) {
        AttendanceEntity attendanceEntity = attendanceRepository.findById(attendanceId).orElseThrow(
                () -> new AppException(ErrorCode.ATTENDANCE_NOT_FOUND)
        );

        attendanceEntity.setStatus(request.getStatus());
        attendanceEntity.setNote(request.getNote());

        //get session by id
        SessionEntity sessionEntity = sessionRepository.findById(request.getSessionId()).orElseThrow(
                () -> new AppException(ErrorCode.SESSION_NOT_FOUND)
        );

        attendanceEntity.setSessionEntity(sessionEntity);

        attendanceRepository.save(attendanceEntity);

        return getAttendance(attendanceId);
    }

    @Override
    public void deleteAttendance(String attendanceId) {
        attendanceRepository.deleteById(attendanceId);
    }
}
