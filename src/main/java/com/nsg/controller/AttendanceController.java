package com.nsg.controller;

import com.nsg.dto.request.attendance.AttendanceRequest;
import com.nsg.dto.response.ApiResponse;
import com.nsg.dto.response.attendance.AttendanceResponse;
import com.nsg.dto.response.student.StudentResponse;
import com.nsg.service.AttendanceService;
import com.nsg.service.StudentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/staff")
@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@SecurityRequirement(name = "Authorization")
public class AttendanceController {

    @Autowired
    AttendanceService attendanceService;

    @Autowired
    StudentService studentService;
    /**********************************
     * Manage Attendance
     **********************************/
    //get student by class Id
    @GetMapping("/get-students-class")
    public ApiResponse<Page<StudentResponse>> viewStudentByBatchNameAndClassName(@RequestParam int page,
                                                                                 @RequestParam int size,
                                                                                 @RequestParam String classId) {

        Page<StudentResponse> studentList = studentService.getStudentByClassId(page, size, classId);
        return ApiResponse.<Page<StudentResponse>>builder()
                .result(studentList)
                .build();
    }

    //create new attendance
    @PostMapping("/create-attendance")
    public ApiResponse<?> createAttendance(@RequestBody AttendanceRequest request) {
        attendanceService.createAttendance(request);
        return ApiResponse.builder()
                .message("Create new attendance successfully!")
                .build();
    }

    //create all attendance for one student in class
    @PostMapping("/create-students-attendances")
    public ApiResponse<?> createAttendanceForOneStudent(@RequestParam String studentId) {
        attendanceService.createAttendanceForOneStudent( studentId );
        return ApiResponse.builder()
                .message("Create attendance for student successfully!")
                .build();
    }

    //get all attendance
    @GetMapping("/get-all-attendance")
    public ApiResponse<Page<AttendanceResponse>> getAllAttendance(@RequestParam int page, @RequestParam int size) {
        return ApiResponse.<Page<AttendanceResponse>>builder()
                .result(attendanceService.getAllAttendance(page, size))
                .build();
    }

    //get attendance by id
    @GetMapping("/get-attendance/{attendance_id}")
    public ApiResponse<AttendanceResponse> getAttendanceById(@PathVariable("attendance_id") String attendance_id) {
        return ApiResponse.<AttendanceResponse>builder()
                .result(attendanceService.getAttendance(attendance_id))
                .build();
    }

    //get attendance by session
    @GetMapping("/get-attendance-session/{session_id}")
    public ApiResponse<Page<AttendanceResponse>> getAttendanceBySession(@PathVariable("session_id") String session_id,
                                                                        @RequestParam int page,
                                                                        @RequestParam int size) {
        return ApiResponse.<Page<AttendanceResponse>>builder()
                .result(attendanceService.getAttendanceBySession(session_id, page, size))
                .build();
    }

    //get attendance by student
    @GetMapping("/get-attendance-student/{student_id}")
    public ApiResponse<Page<AttendanceResponse>> getAttendanceByStudent(@PathVariable("student_id") String student_id,
                                                                        @RequestParam int page,
                                                                        @RequestParam int size) {
        return ApiResponse.<Page<AttendanceResponse>>builder()
                .result(attendanceService.getAttendanceByStudent(student_id, page, size))
                .build();
    }

    //update attendance
    @PostMapping("/update-attendance/{attendance_id}")
    public ApiResponse<AttendanceResponse> updateSessionById(@PathVariable("attendance_id") String attendance_id, @RequestBody AttendanceRequest request) {
        return ApiResponse.<AttendanceResponse>builder()
                .result(attendanceService.updateAttendance(attendance_id, request))
                .build();
    }

    //delete attendance
    @DeleteMapping("/delete-attendance/{attendance_id}")
    public ApiResponse<?> deleteAttendanceById(@PathVariable("attendance_id") String attendance_id) {
        attendanceService.deleteAttendance(attendance_id);
        return ApiResponse.builder()
                .message("Delete attendance successfully!")
                .build();
    }
}
