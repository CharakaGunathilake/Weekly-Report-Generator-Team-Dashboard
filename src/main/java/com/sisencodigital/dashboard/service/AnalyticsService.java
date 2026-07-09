package com.sisencodigital.dashboard.service;

import com.sisencodigital.dashboard.dto.response.SummaryMetricsResponse;
import com.sisencodigital.dashboard.dto.response.VisualInsightsResponse;

public interface AnalyticsService {
    SummaryMetricsResponse getSummaryMetrics(String weekCode);
    VisualInsightsResponse getVisualInsights(String weekCode);
}
