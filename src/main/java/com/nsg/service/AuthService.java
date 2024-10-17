package com.nsg.service;

import com.nsg.dto.request.user.AuthRequest;
import com.nsg.dto.request.user.RegisterRequest;
import com.nsg.dto.response.user.AuthResponse;
import com.nsg.dto.response.user.RegisterResponse;

public interface AuthService {
     AuthResponse authenticate(AuthRequest request);
     RegisterResponse register(RegisterRequest request);
}
