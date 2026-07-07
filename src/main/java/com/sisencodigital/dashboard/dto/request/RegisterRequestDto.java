package com.sisencodigital.dashboard.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequestDto(
        @NotBlank(message = "Name is required")
        @Size(min = 1, max = 100, message = "Name must be 1–100 characters")
        @Pattern(
                regexp = "^(?! )(?!.*  )[\\p{L} ]+(?<! )$",
                message = "Name can contain only letters and single spaces, without leading, trailing, or repeated spaces"
        )
        String name,

        @Email(message = "Email is invalid")
        @NotBlank(message = "Email is required")
        String email,

        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "Password should contain 8+ uppercase and lowercase characters, letters, numbers, special characters"
        )
        @NotBlank(message = "Password is required")
        String password,

        @NotBlank(message = "Role is required")
        String role
) {
}
