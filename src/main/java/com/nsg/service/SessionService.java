package com.nsg.service;

import com.nsg.dto.request.session.ScheduleCreationRequest;
import com.nsg.dto.request.session.SessionCreattionRequest;
import com.nsg.dto.response.session.SessionResponse;
import com.nsg.dto.response.user.UserInforResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SessionService {

    //create session
    void createSession(SessionCreattionRequest request);

    //create schedule
    void createSchedule(String class_id, ScheduleCreationRequest request);

    //get available teacher for each session
    List<UserInforResponse> getAvailableTeachers(String sessionId);

    //delete schedule
    void deleteSchedule(String class_id);

    //get all session
    Page<SessionResponse> getAllSession(int page, int size);

    //get session by class and week (get schedule by week)
    List<SessionResponse> getSessionByClassAndWeek(int week, String className);

    List<SessionResponse> getSessionByClassAndTeacher(String teacherId);

    //get a session
    SessionResponse getSession(String sessionId);

    //update a session
    SessionResponse updateSession(String sessionId, SessionCreattionRequest request);

    //delete session
    void deleteSession(String sessionId);

}
