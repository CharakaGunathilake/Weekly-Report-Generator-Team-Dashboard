package com.sisencodigital.dashboard.dto.response;

import java.util.List;

public record ProjectResponse(
        Long id,
        String name,
        String description,
        List<String> teamMembers
) {}
