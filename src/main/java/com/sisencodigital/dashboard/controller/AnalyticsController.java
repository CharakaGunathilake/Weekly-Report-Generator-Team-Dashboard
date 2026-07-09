package com.sisencodigital.dashboard.controller;

import com.sisencodigital.dashboard.dto.response.SummaryMetricsResponse;
import com.sisencodigital.dashboard.dto.response.VisualInsightsResponse;
import com.sisencodigital.dashboard.service.AnalyticsService;
import com.sisencodigital.dashboard.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/analytics")
@PreAuthorize("hasRole('MANAGER')")
public class AnalyticsController {

    private final AnalyticsService analyticsService;


    @GetMapping("/summary")
    public ApiResponse<SummaryMetricsResponse> getSummaryMetrics(@RequestParam String weekCode) {
        return ApiResponse.success(
                200,
                "Summary retrieved successfully",
                analyticsService.getSummaryMetrics(weekCode)
        );
    }

    @GetMapping("/charts")
    public ApiResponse<VisualInsightsResponse> getVisualInsights(@RequestParam String weekCode) {
        return ApiResponse.success(
                200,
                "Chart data retrieved successfully",
                analyticsService.getVisualInsights(weekCode)
        );
    }
}
