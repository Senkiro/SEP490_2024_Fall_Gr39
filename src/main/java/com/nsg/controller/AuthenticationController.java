package com.nsg.controller;


import com.nsg.dto.request.batch.BatchCreationRequest;
import com.nsg.dto.request.user.AuthRequest;
import com.nsg.dto.request.user.ForgetPasswordRequest;
import com.nsg.dto.request.user.RegisterRequest;
import com.nsg.dto.response.ApiResponse;
import com.nsg.dto.response.user.AuthResponse;
import com.nsg.dto.response.user.RegisterResponse;
import com.nsg.entity.Batch;
import com.nsg.entity.UserEntity;
import com.nsg.service.BatchService;
import com.nsg.service.UserService;
import com.nsg.service.imp.AuthServiceImp;


import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.List;

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

    @Autowired
    BatchService batchService;
    //test batch
    @GetMapping("/batch")
    List<Batch> getAllBatch(){
        return batchService.getAllBatch();
    }

    @PostMapping("/get-batch")
    Batch saveBatch(@RequestBody BatchCreationRequest request){
        batchService.saveBatch(request);
        Batch batch = batchService.getBatch(request.getBatchName());
        return batch;
    }

}
