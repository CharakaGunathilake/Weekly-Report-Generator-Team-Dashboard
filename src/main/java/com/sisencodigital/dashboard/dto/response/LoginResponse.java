package com.sisencodigital.dashboard.dto.response;

public record LoginResponse(
        String token,
        String name,
        String role
) {
}
