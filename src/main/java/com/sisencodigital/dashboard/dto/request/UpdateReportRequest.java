package com.sisencodigital.dashboard.dto.request;

public record UpdateReportRequest(
        String tasksCompleted,
        String nextWeekTasks,
        String blockers,
        Integer hoursWorked,
        Boolean isSubmitted
) {
}
