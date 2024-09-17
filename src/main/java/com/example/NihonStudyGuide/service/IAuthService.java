package com.example.NihonStudyGuide.service;

import com.example.NihonStudyGuide.dto.request.user.AuthRequest;
import com.example.NihonStudyGuide.dto.request.user.RegisterRequest;
import com.example.NihonStudyGuide.dto.response.user.AuthResponse;
import com.example.NihonStudyGuide.dto.response.user.RegisterResponse;

public interface IAuthService {
     AuthResponse authenticate(AuthRequest request);
     RegisterResponse register(RegisterRequest request);
}
