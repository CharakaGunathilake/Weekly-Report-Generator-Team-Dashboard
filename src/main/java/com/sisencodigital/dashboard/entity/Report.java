package com.sisencodigital.dashboard.entity;


import com.sisencodigital.dashboard.enums.ReportStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Builder
@Table(name = "reports")
@NoArgsConstructor
@AllArgsConstructor
public class Report extends BaseEntity {
    @Column(name = "week_code", nullable = false)
    private String weekCode;
    @Column(name = "week_start_date", nullable = false)
    private Date weekStartDate;
    @Column(name = "week_end_date", nullable = false)
    private Date weekEndDate;
    @Column(name = "tasks_completed", nullable = false)
    private String tasksCompleted;
    @Column(name = "next_week_tasks")
    private String nextWeekTasks;
    @Column(name = "blockers")
    private String blockers;
    @Column(name = "hours_worked")
    private Integer hoursWorked;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReportStatus status;
    private Instant submittedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
}
