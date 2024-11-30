package com.nsg.controller;

import com.nsg.common.enums.UserRole;
import com.nsg.dto.request.lesson.LessonCreateRequest;
import com.nsg.dto.request.student.StudentCreattionRequest;
import com.nsg.dto.request.user.UserInforUpdateRequest;
import com.nsg.dto.response.ApiResponse;
import com.nsg.dto.response.classResponse.ClassResponse;
import com.nsg.dto.response.session.SessionResponse;
import com.nsg.dto.response.student.StudentResponse;
import com.nsg.dto.response.user.UserInforResponse;
import com.nsg.service.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teacher")
@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@SecurityRequirement(name = "Authorization")
public class TeacherController {
    @Autowired
    TeacherService teacherService;

    @Autowired
    UserService userService;

    @Autowired
    ClassService classService;

    @Autowired
    StudentService studentService;

    @Autowired
    SessionService sessionService;

    @GetMapping("/detail/{teacher_id}")
    public ApiResponse<UserInforResponse> teacherDetail(@PathVariable("teacher_id") String teacher_id){
        return ApiResponse.<UserInforResponse>builder()
                .result(userService.getUserInforById(teacher_id))
                .build();
    }

//    @PostMapping("/update-teacher/{teacher_id}")
//    ApiResponse<UserInforResponse> updateTeacherInfor(@PathVariable("teacher_id") String teacherId,
//                                                      @RequestBody @Valid @Validated UserInforUpdateRequest request) {
//        return  ApiResponse.<UserInforResponse>builder()
//                .result(userService.updateUserInfor(teacherId, request))
//                .message("update information successfully!")
//                .build();
//    }

    //get a class list
    @GetMapping("/get-class-teacher/{teacher_id}")
    public ApiResponse<List<ClassResponse>> getClassByTeacherId(@PathVariable("teacher_id") String teacherId) {
        return ApiResponse.<List<ClassResponse>>builder()
                .result(classService.getClassByTeacherId(teacherId))
                .build();
    }

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

    // get session by teacher id
    @GetMapping("/get-session-by-teacher")
    public ApiResponse<List<SessionResponse>> getSessionByTeacher(@RequestParam String teacher_id) {
        return ApiResponse.<List<SessionResponse>>builder()
                .result( sessionService.getSessionByClassAndTeacher(teacher_id) )
                .build();
    }


}
