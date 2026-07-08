package com.sisencodigital.dashboard.controller;

import com.sisencodigital.dashboard.dto.response.UserResponse;
import com.sisencodigital.dashboard.service.UserService;
import com.sisencodigital.dashboard.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('TEAM_MEMBER', 'MANAGER')")
    public ApiResponse<UserResponse> getMyProfile (Principal principal){
        return ApiResponse.success(
                201,
                "Profile retrieved successfully for " + principal.getName(),
                userService.getMyProfile(principal)
        );
    }

    @GetMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ApiResponse<List<UserResponse>> getUsers(@RequestParam(required = false) Long userId){
        return ApiResponse.success(
                201,
                "Sign Up successfully",
                userService.getUsers(userId)
        );
    }

}
