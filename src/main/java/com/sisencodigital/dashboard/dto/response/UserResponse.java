package com.sisencodigital.dashboard.dto.response;

import com.sisencodigital.dashboard.enums.UserRole;
import com.sisencodigital.dashboard.enums.UserStatus;

import java.time.Instant;

public record UserResponse(
        Long id,
        String name,
        String email,
        UserRole role,
        UserStatus status,
        Instant lastLoginAt,
        Instant createdAt
) {
}
