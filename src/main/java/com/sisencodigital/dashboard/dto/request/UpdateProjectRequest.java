package com.sisencodigital.dashboard.dto.request;

import java.util.List;

public record UpdateProjectRequest(
        String name,
        String description,
        List<Long> userIds
) {
}
