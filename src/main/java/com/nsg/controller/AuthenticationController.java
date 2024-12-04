package com.nsg.controller;

import com.nsg.dto.request.user.*;
import com.nsg.dto.response.ApiResponse;
import com.nsg.dto.response.user.AuthResponse;
import com.nsg.dto.response.user.RegisterResponse;
import com.nsg.dto.response.user.UserInforResponse;
import com.nsg.entity.UserEntity;
import com.nsg.service.UserService;
import com.nsg.service.imp.AuthServiceImp;


import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//@SecurityRequirement(name = "Authorization")
public class AuthenticationController {
    AuthServiceImp authService;

    UserService userService;

    @PostMapping("/login")
    ApiResponse<AuthResponse> login(@RequestBody AuthRequest request){
        var result = authService.authenticate(request);
        return ApiResponse.<AuthResponse>builder()
                .message("Login Successfully!")
                .result(result).build();
    }

//    @PostMapping("/introspect")
//    ApiResponse<IntrospectResponse> login(@RequestBody IntrospectRequest request)
//            throws ParseException, JOSEException {
//        var result = authService.introspect(request);
//        return ApiResponse.<IntrospectResponse>builder()
//                .result(result).build();
//    }

    @PostMapping("/register")
    ApiResponse<RegisterResponse> register(@RequestBody @Valid RegisterRequest request){
                return ApiResponse.<RegisterResponse>builder()
                        .result(authService.register(request))
                        .message("Register successfully!")
                        .build();
    }

    //send email with new password
    @PostMapping("/forget-password")
    String sendEmail(@RequestBody @Valid ForgetPasswordRequest request) {
        //get email user inputed
        String toEmail = request.getEmail();

        //get User by email
        UserEntity user = userService.getUserByEmail(toEmail);

        return userService.forgetPassword(user, toEmail);

    }

    @PostMapping("/reset-password")
    public ApiResponse<UserInforResponse> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        UserInforResponse updatedUser = userService.updateUserPassword(request);

        return ApiResponse.<UserInforResponse>builder()
                .result(updatedUser)
                .message("Reset password successfully!")
                .build();
    }

    @PutMapping(value = "/update-profile/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<?> updateProfiler(
            @PathVariable("userId") String userId,
            @RequestPart("userDetail") @Valid UserInforUpdateRequest request,
            @RequestPart(value = "avatar", required = false) MultipartFile avatar) {

        var updatedUser = userService.updateUserInfor(userId, request, avatar);

        return ApiResponse.builder()
                .result(updatedUser)
                .message("User updated successfully!")
                .build();
    }

}
