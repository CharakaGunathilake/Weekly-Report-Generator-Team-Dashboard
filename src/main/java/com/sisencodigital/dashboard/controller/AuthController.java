package com.sisencodigital.dashboard.controller;

import com.sisencodigital.dashboard.dto.request.LoginRequest;
import com.sisencodigital.dashboard.dto.request.RegisterRequest;
import com.sisencodigital.dashboard.dto.response.LoginResponse;
import com.sisencodigital.dashboard.dto.response.RegisterResponse;
import com.sisencodigital.dashboard.service.AuthService;
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
    public ApiResponse<RegisterResponse> register(@RequestBody @Valid RegisterRequest request) {
        return ApiResponse.success(
                201,
                "Sign Up successfully",
                authService.register(request)
        );
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        return ApiResponse.success(
                HttpStatus.OK.value(),
                "Login successful",
                authService.login(request)
        );
    }
}
