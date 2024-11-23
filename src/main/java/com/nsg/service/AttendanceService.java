package com.nsg.service;

import com.nsg.dto.request.attendance.AttendanceRequest;
import com.nsg.dto.response.attendance.AttendanceResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface AttendanceService {

    //create
    void createAttendance(AttendanceRequest request);

    //get all
    Page<AttendanceResponse> getAllAttendance(int page, int size);

    //get list by sessionId
    Page<AttendanceResponse> getAttendanceBySession(String sessionId, int page, int size);

    //get by id
    AttendanceResponse getAttendance(String attendanceId);

    //update by id
    AttendanceResponse updateAttendance(String attendanceId, AttendanceRequest request);

    //delete
    void deleteAttendance(String attendanceId);

}
