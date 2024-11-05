package com.nsg.controller;

import com.nsg.common.enums.UserRole;
import com.nsg.dto.request.lesson.LessonCreateRequest;
import com.nsg.dto.request.student.StudentCreattionRequest;
import com.nsg.dto.request.user.UserInforUpdateRequest;
import com.nsg.dto.response.ApiResponse;
import com.nsg.dto.response.staff.StudentResponse;
import com.nsg.dto.response.user.UserInforResponse;
import com.nsg.entity.LessonEntity;
import com.nsg.service.TeacherService;
import com.nsg.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/detail/{teacher_id}")
    public ApiResponse<UserInforResponse> teacherDetail(@PathVariable("teacher_id") String teacher_id){
        return ApiResponse.<UserInforResponse>builder()
                .result(userService.getUserInforById(teacher_id))
                .build();
    }

    @PostMapping("/update-teacher/{teacher_id}")
    ApiResponse<UserInforResponse> updateTeacherInfor(@PathVariable("teacher_id") String teacherId,
                                                      @RequestBody @Valid @Validated UserInforUpdateRequest request) {
        return  ApiResponse.<UserInforResponse>builder()
                .result(userService.updateUserInfor(teacherId, request))
                .message("update information successfully!")
                .build();
    }


}
