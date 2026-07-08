package com.sisencodigital.dashboard.service;

import com.sisencodigital.dashboard.dto.response.UserResponse;

import java.security.Principal;
import java.util.List;

public interface UserService {
    UserResponse getMyProfile(Principal principal);
    List<UserResponse> getUsers (Long userId);
}
