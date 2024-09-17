package com.example.NihonStudyGuide.controller;


import com.example.NihonStudyGuide.dto.request.user.AuthRequest;
import com.example.NihonStudyGuide.dto.request.user.RegisterRequest;
import com.example.NihonStudyGuide.dto.response.ApiResponse;
import com.example.NihonStudyGuide.dto.response.user.AuthResponse;
import com.example.NihonStudyGuide.dto.response.user.RegisterResponse;
import com.example.NihonStudyGuide.service.Imp.AuthServiceImp;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//@SecurityRequirement(name = "Authorization")
public class AuthenticationController {
    AuthServiceImp authService;
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

}
