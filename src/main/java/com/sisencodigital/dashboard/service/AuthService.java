package com.sisencodigital.dashboard.service;

import com.sisencodigital.dashboard.dto.request.LoginRequest;
import com.sisencodigital.dashboard.dto.request.RegisterRequest;
import com.sisencodigital.dashboard.dto.response.LoginResponse;
import com.sisencodigital.dashboard.dto.response.RegisterResponse;

public interface AuthService {
    RegisterResponse register(RegisterRequest registerRequestDto);
    LoginResponse login(LoginRequest loginRequestDto);
}
