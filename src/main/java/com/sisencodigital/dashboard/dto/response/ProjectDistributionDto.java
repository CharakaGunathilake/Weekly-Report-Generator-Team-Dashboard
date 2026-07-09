package com.sisencodigital.dashboard.dto.response;

public record ProjectDistributionDto(
        String projectName,
        long totalHoursWorked,
        long reportCount
) {}
