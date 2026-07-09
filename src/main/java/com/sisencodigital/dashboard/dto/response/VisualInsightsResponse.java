package com.sisencodigital.dashboard.dto.response;

import java.util.List;

public record VisualInsightsResponse(
        List<ChartTrendDto> trendOverTime,
        List<StatusDistributionDto> submissionDistribution,
        List<ProjectDistributionDto> projectDistribution
) {
}
