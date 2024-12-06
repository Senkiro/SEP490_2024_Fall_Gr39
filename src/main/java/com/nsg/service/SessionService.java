package com.nsg.service;

import com.nsg.dto.request.session.ScheduleCreationRequest;
import com.nsg.dto.request.session.SessionCreattionRequest;
import com.nsg.dto.request.session.SessionUpdateRequest;
import com.nsg.dto.response.session.SessionResponse;
import com.nsg.dto.response.user.UserInforResponse;
import com.nsg.entity.SessionEntity;
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

    //get unavailable session to change date
    List<SessionResponse> getSessionUnavailable(String classId, int sessionWeek);

    //delete schedule
    void deleteSchedule(String class_id);

    //get all session
    Page<SessionResponse> getAllSession(int page, int size);

    //get session by class and week (get schedule by week)
    List<SessionResponse> getSessionByClassAndWeek(int week, String className);

    List<SessionResponse> getSessionByClassAndTeacher(String teacherId);

    List<SessionResponse> getSessionByAttendanceStatus(String classId);

    //get a session
    SessionResponse getSession(String sessionId);

    //get session by studentId
    public List<SessionResponse> getSessionByStudentId(String studentId);

    //update only session status
    void updateOnlySessionStatus(String sessionId);

    //update a session
    SessionResponse updateSession(String sessionId, SessionUpdateRequest request);

    //delete session
    void deleteSession(String sessionId);

    List<SessionResponse> getSessionByExamNotNull(String classId);

    List<SessionResponse> getSessionByExamNotNullAndTeacherId(String classId, String teacherId);

    void updateSessionAttendanceStatus(String sessionId, String newStatus);

    void updateSessionMarkStatus(String sessionId, String newStatus);

    SessionResponse toSessionResponse(SessionEntity sessionEntity);

    void swapToUnavailableSession(String currentSessionId, String toSessionId);

    void autoFillTeacherToSession(String teacherId,
                                  String classId,
                                  String sessionId,
                                  int weekStart,
                                  int weekEnd);

}
