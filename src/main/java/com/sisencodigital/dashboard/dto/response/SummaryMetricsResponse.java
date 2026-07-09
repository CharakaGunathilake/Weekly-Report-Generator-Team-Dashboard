package com.sisencodigital.dashboard.dto.response;

public record SummaryMetricsResponse(
        long totalReportsSubmitted,
        double submissionComplianceRate,
        long openBlockersCount
) {
}
