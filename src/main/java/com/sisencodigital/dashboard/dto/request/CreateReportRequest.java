package com.sisencodigital.dashboard.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.Date;

public record CreateReportRequest(
        // WeekCode is a unique identifier for the week, with the format of "W1", "W10", "W28", etc.
        @NotBlank(message = "Week code is required")
                @Pattern(regexp = "^W\\d{1,2}$", message = "Week code must be in the format of 'W1', 'W10', 'W28', etc.")
        String weekCode,
        @NotNull(message = "Week start date is required")
        Date weekStartDate,
        @NotNull(message = "Week end date is required")
        Date weekEndDate,
        @NotBlank(message = "Tasks completed is required")
        String tasksCompleted,
        String nextWeekTasks,
        String blockers,
        Integer hoursWorked,
        @NotNull(message = "Project ID is required")
        Long projectId
) {
}