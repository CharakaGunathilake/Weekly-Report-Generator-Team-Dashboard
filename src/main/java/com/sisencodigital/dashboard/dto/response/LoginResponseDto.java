package com.sisencodigital.dashboard.dto.response;

public record LoginResponseDto(
        String token,
        String name,
        String role
) {
}
