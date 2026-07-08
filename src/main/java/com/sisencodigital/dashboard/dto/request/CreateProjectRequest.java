package com.sisencodigital.dashboard.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateProjectRequest(
        @NotBlank(message = "Name is required")
        @Size(min = 1, max = 100, message = "Name must be 1–100 characters")
        String name,
        String description,
        List<Long> userIds
) {
}
