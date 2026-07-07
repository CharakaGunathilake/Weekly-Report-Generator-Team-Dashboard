package com.sisencodigital.dashboard.controller;

import com.sisencodigital.dashboard.dto.request.LoginRequestDto;
import com.sisencodigital.dashboard.dto.request.RegisterRequestDto;
import com.sisencodigital.dashboard.dto.response.LoginResponseDto;
import com.sisencodigital.dashboard.dto.response.RegisterResponseDto;
import com.sisencodigital.dashboard.service.AuthService;
import com.sisencodigital.dashboard.service.UserService;
import com.sisencodigital.dashboard.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ApiResponse<RegisterResponseDto> register(@RequestBody @Valid RegisterRequestDto request) {
        return ApiResponse.success(
                201,
                "Sign Up successfully",
                authService.register(request)
        );
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto request) {
        return ApiResponse.success(
                HttpStatus.OK.value(),
                "Login successful",
                authService.login(request)
        );
    }
}
