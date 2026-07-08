package com.sisencodigital.dashboard.util;

import com.sisencodigital.dashboard.dto.response.ReportResponse;
import com.sisencodigital.dashboard.entity.Report;

public class ReportMapper {
    private ReportMapper() {
    }

    public static ReportResponse convertToResponseDto(Report report) {
        return new ReportResponse(
                report.getId(),
                report.getWeekCode(),
                report.getWeekStartDate(),
                report.getWeekEndDate(),
                report.getTasksCompleted(),
                report.getNextWeekTasks(),
                report.getBlockers(),
                report.getHoursWorked(),
                report.getStatus().name(),
                report.getSubmittedAt() != null ? report.getSubmittedAt() : null,
                report.getProject().getId(),
                report.getUser().getId()
        );
    }
}
