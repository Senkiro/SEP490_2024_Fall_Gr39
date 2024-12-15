package com.nsg.controller;

import com.nsg.common.enums.UserRole;
import com.nsg.dto.request.user.UserCreationRequest;
import com.nsg.dto.request.user.UserUpdateRequest;
import com.nsg.dto.response.ApiResponse;
import com.nsg.dto.response.attendance.AttendanceResponse;
import com.nsg.dto.response.session.SessionResponse;
import com.nsg.dto.response.user.UserFullDetailsResponse;
import com.nsg.dto.response.user.UserInforResponse;
import com.nsg.entity.UserEntity;
import com.nsg.service.AttendanceService;
import com.nsg.service.SessionService;
import com.nsg.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@SecurityRequirement(name = "Authorization")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    @Autowired
    private final UserService userService;

    @Autowired
    SessionService sessionService;

    @PostMapping("/create-user")
    public ApiResponse<?> createUser(@RequestBody @Valid UserCreationRequest request, @RequestParam UserRole role) {
        userService.createUser(request, role);
        return ApiResponse.<UserInforResponse>builder()
                .message("Create new user successfully!")
                .build();
    }

    //get all users in database
    @GetMapping()
    List<UserEntity> getAllUser() {
        return userService.getAllUser();
    }

    //get users by role
    @GetMapping("/get-users-by-role")
    public ApiResponse<Page<UserFullDetailsResponse>> getUsersByRoles(@RequestParam UserRole role, int page, int size) {
        return ApiResponse.<Page<UserFullDetailsResponse>>builder()
                .result(userService.getUsersByRoles(role, page, size))
                .build();
    }

    //set user to not active
    @GetMapping("/change-users-active")
    public ApiResponse<?> changeUsersActive(@RequestParam String userId) {
        userService.changeUsersActive(userId);
        return ApiResponse.builder()
                .message("User active's have been changed!")
                .build();
    }

    //get user by userId
    @GetMapping("/{userId}")
    public ApiResponse<UserInforResponse> userDetail(@PathVariable("userId") String userId){
        return ApiResponse.<UserInforResponse>builder()
                .result(userService.getUserInforById(userId))
                .build();
    }

    //update user
    @PutMapping("/{userId}")
    UserEntity updateUserPassword(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        return userService.updateUserPass(userId, request);
    }

    //Delete user
    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return "User have been deleted";
    }

    //get attendance by batch status
    @GetMapping("/get-session-by-batch-status")
    public ApiResponse<List<SessionResponse>> getAttendanceByBatchStatus() {
        return ApiResponse.<List<SessionResponse>>builder()
                .result(sessionService.getSessionInBatch())
                .build();
    }

}
