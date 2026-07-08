package com.sisencodigital.dashboard.dto.response;

import com.sisencodigital.dashboard.enums.UserRole;
import com.sisencodigital.dashboard.enums.UserStatus;
public record RegisterResponse(
        Long id,
        String name,
        String email,
        UserRole role,
        UserStatus status
) {
}
