package com.nsg.service;

import com.nsg.dto.request.attendance.AttendanceRequest;
import com.nsg.dto.response.attendance.AttendanceResponse;
import com.nsg.dto.response.attendance.AttendanceStatisticsResponse;
import com.nsg.entity.StudentEntity;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AttendanceService {

    //create
    void createAttendance(AttendanceRequest request);

    //create attendance for schedule
    void createAttendancesForSession(String classId);

    void createAttendanceForOneStudent(String studentId);


    void createAttendanceForStudentsInClass(String classId, List<StudentEntity> studentEntityList);

    //get all
    Page<AttendanceResponse> getAllAttendance(int page, int size);

    //get list by sessionId
    Page<AttendanceResponse> getAttendanceBySession(String sessionId, int page, int size);

    Page<AttendanceResponse> getAttendanceByStudent(String studentId, int page, int size);

    //get by id
    AttendanceResponse getAttendance(String attendanceId);

    //update by id
    AttendanceResponse updateAttendance(String attendanceId, AttendanceRequest request);

    //delete
    void deleteAttendance(String attendanceId);

    AttendanceStatisticsResponse getDataAttendanceStatisticsResponse(String studentId);

}
