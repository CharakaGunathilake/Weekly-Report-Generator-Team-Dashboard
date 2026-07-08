package com.sisencodigital.dashboard.dto.response;

import java.time.Instant;
import java.util.Date;

public record ReportResponse(
        Long id,
        String weekCode,
        Date weekStartDate,
        Date weekEndDate,
        String tasksCompleted,
        String nextWeekTasks,
        String blockers,
        Integer hoursWorked,
        String status,
        Instant submittedAt,
        Long projectId,
        Long userId
) {
}
