package com.sisencodigital.dashboard.service;

import com.sisencodigital.dashboard.dto.request.LoginRequestDto;
import com.sisencodigital.dashboard.dto.request.RegisterRequestDto;
import com.sisencodigital.dashboard.dto.response.LoginResponseDto;
import com.sisencodigital.dashboard.dto.response.RegisterResponseDto;

public interface AuthService {
    RegisterResponseDto register(RegisterRequestDto registerRequestDto);
    LoginResponseDto login(LoginRequestDto loginRequestDto);
}
