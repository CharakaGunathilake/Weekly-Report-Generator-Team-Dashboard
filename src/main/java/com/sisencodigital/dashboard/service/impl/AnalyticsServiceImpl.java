package com.sisencodigital.dashboard.service.impl;

import com.sisencodigital.dashboard.dto.response.*;
import com.sisencodigital.dashboard.enums.ReportStatus;
import com.sisencodigital.dashboard.repository.ReportRepository;
import com.sisencodigital.dashboard.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalyticsServiceImpl implements AnalyticsService {

    private final ReportRepository reportRepository;


    @Override
    public SummaryMetricsResponse getSummaryMetrics(String weekCode) {
        List<Object[]> statusResults = reportRepository.getStatusCountsByWeek(weekCode);

        long submittedCount = 0;
        long totalCount = 0;

        for (Object[] result : statusResults) {
            ReportStatus status = (ReportStatus) result[0];
            long count = (long) result[1];

            totalCount += count;
            if (status == ReportStatus.SUBMITTED || status == ReportStatus.LATE) {
                submittedCount += count; // Counts late reports as submitted
            }
        }

        double complianceRate = (totalCount == 0) ? 0.0 : ((double) submittedCount / totalCount) * 100.0;
        long openBlockers = reportRepository.countOpenBlockersByWeek(weekCode);

        return new SummaryMetricsResponse(submittedCount, Math.round(complianceRate * 10.0) / 10.0, openBlockers);
    }

    @Override
    public VisualInsightsResponse getVisualInsights(String weekCode) {
        List<ChartTrendDto> trendData = reportRepository.getHistoricalTrendOverTime().stream()
                .map(row -> new ChartTrendDto((long) row[1], (long) row[2]))
                .toList();

        List<Object[]> statusResults = reportRepository.getStatusCountsByWeek(weekCode);
        List<StatusDistributionDto> statusDistribution = new ArrayList<>();
        for (Object[] row : statusResults) {
            statusDistribution.add(new StatusDistributionDto(((ReportStatus) row[0]).name(), (long) row[1]));
        }

        List<ProjectDistributionDto> projectDistribution = reportRepository.getWorkloadDistributionByProject(weekCode).stream()
                .map(row -> new ProjectDistributionDto((String) row[0], (long) row[1], (long) row[2]))
                .toList();

        return new VisualInsightsResponse(trendData, statusDistribution, projectDistribution);
    }
}