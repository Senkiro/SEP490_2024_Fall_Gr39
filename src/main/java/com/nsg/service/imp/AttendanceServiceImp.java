package com.nsg.service.imp;

import com.nsg.common.exception.AppException;
import com.nsg.common.exception.ErrorCode;
import com.nsg.dto.request.attendance.AttendanceRequest;
import com.nsg.dto.response.attendance.AttendanceResponse;
import com.nsg.dto.response.attendance.AttendanceStatisticsResponse;
import com.nsg.dto.response.attendance.DailyAttendanceAndMarksSummaryResponse;
import com.nsg.dto.response.batch.BatchResponse;
import com.nsg.dto.response.student.StudentResponse;
import com.nsg.entity.AttendanceEntity;
import com.nsg.entity.BatchEntity;
import com.nsg.entity.SessionEntity;
import com.nsg.entity.StudentEntity;
import com.nsg.repository.*;
import com.nsg.service.AttendanceService;
import com.nsg.service.MarkService;
import com.nsg.service.SessionService;
import com.nsg.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    @Autowired
    SessionService sessionService;

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
        // get all student in class
        List<StudentEntity> studentEntityList = studentRepository.findByClassEntityClassId(classId);
        createAttendanceForStudentsInClass(classId, studentEntityList);

    }

    //create attendance for students in a class
    @Override
    public void createAttendanceForStudentsInClass(String classId, List<StudentEntity> studentEntityList) {
        //check class existed or not
        if (!classRepository.existsById(classId)) {
            throw new AppException(ErrorCode.CLASS_NOT_FOUND);
        }

        //if there are no student in list
        if (studentEntityList.isEmpty()) {
            throw new AppException(ErrorCode.STUDENT_LIST_IS_EMPTY);
        }

        // get session by classId
        //get all session available by classId, then sort it by day and sessionNumber
        List<SessionEntity> availableSessions = sessionRepository.findSessionsByClassIdAndAvailableAndStatus(classId);

        //check session
        if (availableSessions.isEmpty()) {
            //There are no session for this class
            throw new AppException(ErrorCode.SCHEDULE_EMPTY);
        }

        // list AttendanceRequest
        List<AttendanceRequest> createRequest = new ArrayList<>();
        String defaultStatus = "Not happen";

        for (SessionEntity session : availableSessions) {
            for (StudentEntity student : studentEntityList) {
                AttendanceRequest attendanceRequest = new AttendanceRequest();

                attendanceRequest.setStatus(defaultStatus);
                attendanceRequest.setSessionId(session.getSessionId());

                if (student.getStudentId() != null) {
                    attendanceRequest.setStudentId(student.getStudentId());
                } else {

                }
                createRequest.add(attendanceRequest);
            }
        }
        // call create attendance function
        for (AttendanceRequest request : createRequest) {
            createAttendance(request);
        }
    }

    @Override
    public void createAttendanceForOneStudent(String studentId) {
        //get student
        StudentEntity student = studentRepository.findById(studentId).orElseThrow(
                () -> new AppException(ErrorCode.STUDENT_NOT_FOUND)
        );

        //check exist?
        if (attendanceRepository.existsByStudentId(studentId)) {
            throw new AppException(ErrorCode.ATTENDANCE_EXISTED);
        }

        //get all session available by classId, then sort it by day and sessionNumber
        List<SessionEntity> availableSessions = sessionRepository.findSessionsByClassIdAndAvailableAndStatus( student.getClassEntity().getClassId() );

        //check session
        if (availableSessions.isEmpty()) {
            //There are no session for this class
            throw new AppException(ErrorCode.SCHEDULE_EMPTY);
        }

        // list AttendanceRequest
        List<AttendanceRequest> createRequest = new ArrayList<>();
        String defaultStatus = "Not happen";

        for (SessionEntity session : availableSessions) {
                AttendanceRequest attendanceRequest = new AttendanceRequest();

                attendanceRequest.setStatus(defaultStatus);
                attendanceRequest.setSessionId(session.getSessionId());
                attendanceRequest.setStudentId( studentId );

                createRequest.add(attendanceRequest);
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

    @Override
    public List<AttendanceResponse> getAttendanceByBatchStatus(int batchStatus) {
        List<AttendanceEntity> attendanceEntities = attendanceRepository.findAttendanceByBatchStatus(batchStatus);
        List<AttendanceResponse> responseList = convertToListAttendanceResponse(attendanceEntities);

        return responseList;
    }

    public List<AttendanceResponse> convertToListAttendanceResponse(List<AttendanceEntity> attendanceEntities) {
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
                attendanceResponse.setTeacherName(session.getUser().getFullName());
            }

            attendanceResponse.setStudentResponse(studentResponse);
            attendanceResponse.setSessionResponse(sessionService.toSessionResponse(attendance.getSessionEntity()));

            attendanceResponse.setSessionResponse(sessionService.toSessionResponse(attendance.getSessionEntity()));

            responseList.add(attendanceResponse);
        }

        return responseList;
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
                attendanceResponse.setTeacherName(session.getUser().getFullName());
            }

            attendanceResponse.setStudentResponse(studentResponse);
            attendanceResponse.setSessionResponse(sessionService.toSessionResponse(attendance.getSessionEntity()));

            attendanceResponse.setSessionResponse(sessionService.toSessionResponse(attendance.getSessionEntity()));

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

    //convert attendance statistic data
    @Override
    public AttendanceStatisticsResponse getDataAttendanceStatisticsResponse(String studentId) {
        //check existed attendance for this student
        if (attendanceRepository.existsByStudentId(studentId)) {
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
        } else {
            throw new AppException(ErrorCode.NO_ATTENDANCE_FOR_STUDENT);
        }

    }

    //calculate all student attend in one day
    @Override
    public int calculateAttendedStudentInDay(LocalDate date) {

        int numberOfAttendedStudent = 0;

        numberOfAttendedStudent += attendanceRepository.countAttendanceByDate(date);

        return numberOfAttendedStudent;
    }
}
