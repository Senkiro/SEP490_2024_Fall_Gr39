package com.nsg.controller;

import com.nsg.dto.request.session.ScheduleCreationRequest;
import com.nsg.dto.request.session.SessionCreattionRequest;
import com.nsg.dto.request.session.SessionUpdateRequest;
import com.nsg.dto.response.ApiResponse;
import com.nsg.dto.response.room.RoomResponse;
import com.nsg.dto.response.session.SessionResponse;
import com.nsg.dto.response.user.UserInforResponse;
import com.nsg.service.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/staff")
@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@SecurityRequirement(name = "Authorization")
public class ScheduleController {
    @Autowired
    SessionService sessionService;

    @Autowired
    AttendanceService attendanceService;

    @Autowired
    MarkService markService;

    @Autowired
    RoomService roomService;

    /**********************************
     * Manage Session
     **********************************/
    //create a class
    @PostMapping("/create-session")
    public ApiResponse<?> createSession(@RequestBody SessionCreattionRequest request) {
        sessionService.createSession(request);
        return ApiResponse.builder()
                .message("Create new session successfully!")
                .build();
    }

    //create a schedule
    @PostMapping("/create-schedule/{class_id}")
    public ApiResponse<?> createSchedule(@PathVariable("class_id") String class_id, @RequestBody ScheduleCreationRequest request) {
        sessionService.createSchedule(class_id, request);
        attendanceService.createAttendancesForSession(class_id);
        markService.generateMarkForAllStudentInClass(class_id);
        return ApiResponse.builder()
                .message("Create new schedule successfully!")
                .build();
    }

    //get all session
    @GetMapping("/get-all-session")
    public ApiResponse<Page<SessionResponse>> getAllSession(@RequestParam int page, @RequestParam int size) {
        return ApiResponse.<Page<SessionResponse>>builder()
                .result(sessionService.getAllSession(page, size))
                .build();
    }

    @GetMapping("/get-session-week")
    public ApiResponse<List<SessionResponse>> getSessionByWeek(@RequestParam int week, @RequestParam String class_id) {
        return ApiResponse.<List<SessionResponse>>builder()
                .result(sessionService.getSessionByClassAndWeek(week, class_id))
                .build();
    }

    @GetMapping("/get-session-by-teacher")
    public ApiResponse<List<SessionResponse>> getSessionByTeacher(@RequestParam String teacher_id) {
        return ApiResponse.<List<SessionResponse>>builder()
                .result( sessionService.getSessionByClassAndTeacher(teacher_id) )
                .build();
    }

    //get session by student
    @GetMapping("/get-session-by-student")
    public ApiResponse<List<SessionResponse>> getSessionByStudent(@RequestParam String student_id) {
        return ApiResponse.<List<SessionResponse>>builder()
                .result( sessionService.getSessionByStudentId(student_id) )
                .build();
    }

    //get session which have exam in a class
    @GetMapping("/get-session-have-exam")
    public ApiResponse<List<SessionResponse>> getSessionHaveExam(@RequestParam String class_id) {
        return ApiResponse.<List<SessionResponse>>builder()
                .result( sessionService.getSessionByExamNotNull(class_id) )
                .build();
    }

    //get session which have exam in a class by teacher
    @GetMapping("/get-session-have-exam-and-teacher")
    public ApiResponse<List<SessionResponse>> getSessionHaveExamAndTeacher(@RequestParam String class_id,
                                                                           @RequestParam String teacher_id) {
        return ApiResponse.<List<SessionResponse>>builder()
                .result( sessionService.getSessionByExamNotNullAndTeacherId( class_id, teacher_id ) )
                .build();
    }

    //get session by attendance status
    @GetMapping("/get-session-by-attendance-status")
    public ApiResponse<List<SessionResponse>> getSessionByAttendanceStatus(@RequestParam String class_id) {
        return ApiResponse.<List<SessionResponse>>builder()
                .result( sessionService.getSessionByAttendanceStatus( class_id ) )
                .build();
    }

    //get session by id
    @GetMapping("/get-session/{session_id}")
    public ApiResponse<SessionResponse> getSessionById(@PathVariable("session_id") String session_id) {
        return ApiResponse.<SessionResponse>builder()
                .result(sessionService.getSession(session_id))
                .build();
    }

    //get available teacher for session
    @GetMapping("/get-available-teacher/{session_id}")
    public ApiResponse<List<UserInforResponse>> getAvailableTeacher(@PathVariable("session_id") String session_id) {
        return ApiResponse.<List<UserInforResponse>>builder()
                .result(sessionService.getAvailableTeachers(session_id))
                .build();
    }

    //get unavailable session
    @GetMapping("/get-unavailable-session")
    public ApiResponse<List<SessionResponse>> getUnavailableSesssion(@RequestParam String class_id,
                                                                     @RequestParam int sessionWeek) {
        return ApiResponse.<List<SessionResponse>>builder()
                .result(sessionService.getSessionUnavailable(class_id, sessionWeek))
                .build();
    }

    //update session status only
    @PostMapping("/update-session-status/{session_id}")
    public ApiResponse<?> updateSessionStatus(@PathVariable("session_id") String session_id) {
        sessionService.updateOnlySessionStatus(session_id);
        return ApiResponse.builder()
                .message("Session status updated!")
                .build();
    }

    @PostMapping("/update-session-attendance-status/{session_id}")
    public ApiResponse<?> updateSessionAttendanceStatus(@PathVariable("session_id") String session_id, @RequestParam String new_status) {
        sessionService.updateSessionAttendanceStatus(session_id, new_status);
        return ApiResponse.builder()
                .message("Session attendance status updated!")
                .build();
    }

    @PostMapping("/update-session-mark-status/{session_id}")
    public ApiResponse<?> updateSessionMarkStatus(@PathVariable("session_id") String session_id, @RequestParam String new_status) {
        sessionService.updateSessionMarkStatus(session_id, new_status);
        return ApiResponse.builder()
                .message("Session mark status updated!")
                .build();
    }

    //update
    @PostMapping("/update-session/{session_id}")
    public ApiResponse<SessionResponse> updateSessionById(@PathVariable("session_id") String session_id, @RequestBody SessionUpdateRequest request) {
        return ApiResponse.<SessionResponse>builder()
                .result(sessionService.updateSession(session_id, request))
                .build();
    }

    //delete schedule
    @DeleteMapping("/delete-schedule/{class_id}")
    public ApiResponse<?> deleteSchedule(@PathVariable("class_id") String class_id) {
        sessionService.deleteSchedule(class_id);
        return ApiResponse.builder()
                .message("Delete schedule successfully!")
                .build();
    }

    //delete
    @DeleteMapping("/delete-session/{session_id}")
    public ApiResponse<SessionResponse> deleteSessionById(@PathVariable("session_id") String session_id) {
        sessionService.deleteSession(session_id);
        return ApiResponse.<SessionResponse>builder()
                .message("Delete session successfully!")
                .build();
    }

    //swap session
    @PostMapping("/swap-to-unavailable-session")
    public ApiResponse<?> swapToUnavailableSesssion(@RequestParam String currentSessionId,
                                                    @RequestParam String toSessionId) {
        sessionService.swapToUnavailableSession(currentSessionId, toSessionId);
        return ApiResponse.builder()
                .message("Change session successfully!")
                .build();
    }


    @PostMapping("/auto-fill-teacher")
    public ApiResponse<?> autoFillTeacherIntoSchedule(@RequestParam String teacherId,
                                                    @RequestParam String classId,
                                                      @RequestParam String sessionId,
                                                      @RequestParam int weekEnd ) {
        sessionService.autoFillTeacherToSession(teacherId, classId, sessionId, weekEnd);
        return ApiResponse.builder()
                .message("Fill teacher!")
                .build();
    }

    //get available room for session
    @GetMapping("/get-available-room/{session_id}")
    public ApiResponse<List<RoomResponse>> getAvailableRoom(@PathVariable("session_id") String session_id) {
        return ApiResponse.<List<RoomResponse>>builder()
                .result(roomService.getAvailableRoomForSession(session_id))

                .build();
    }

}
