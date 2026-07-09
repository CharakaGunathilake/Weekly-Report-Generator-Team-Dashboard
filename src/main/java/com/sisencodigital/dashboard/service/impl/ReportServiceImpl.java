package com.sisencodigital.dashboard.service.impl;

import com.sisencodigital.dashboard.dto.request.CreateReportRequest;
import com.sisencodigital.dashboard.dto.request.UpdateReportRequest;
import com.sisencodigital.dashboard.dto.response.ReportResponse;
import com.sisencodigital.dashboard.entity.Project;
import com.sisencodigital.dashboard.entity.Report;
import com.sisencodigital.dashboard.entity.User;
import com.sisencodigital.dashboard.enums.ReportStatus;
import com.sisencodigital.dashboard.exceptions.custom.ReportAlreadyExists;
import com.sisencodigital.dashboard.exceptions.custom.ResourceNotFoundException;
import com.sisencodigital.dashboard.exceptions.custom.WeekCodeAlreadyExistsException;
import com.sisencodigital.dashboard.repository.ProjectRepository;
import com.sisencodigital.dashboard.repository.ReportRepository;
import com.sisencodigital.dashboard.repository.UserRepository;
import com.sisencodigital.dashboard.service.ReportService;
import com.sisencodigital.dashboard.util.ReportMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    @Override
    @Transactional
    public ReportResponse createReport(CreateReportRequest request, Principal principal) {

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + principal.getName()));

        Project project = projectRepository.findById(request.projectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + request.projectId()));

        if (!project.getTeamMembers().contains(user)) {
            throw new IllegalArgumentException("Cannot create report, You are not assigned for the selected project");
        }

        // Validate if a weekCode with same exists with the same userId and projectId
        if (reportRepository.existsByUserIdAndProjectIdAndWeekCode(user.getId(), project.getId(), request.weekCode())) {
            throw new WeekCodeAlreadyExistsException("WeekCode already exists under the same project");
        }


        if (reportRepository.existsByWeekStartDateAndWeekEndDateAndUserIdAndProjectId(
                request.weekStartDate(),
                request.weekEndDate(),
                user.getId(),
                project.getId()
        )) {
            throw new ReportAlreadyExists("Report already exists under the same project");
        }

        Report report = Report.builder()
                .weekCode(request.weekCode().trim())
                .weekStartDate(request.weekStartDate())
                .weekEndDate(request.weekEndDate())
                .tasksCompleted(request.tasksCompleted())
                .nextWeekTasks(request.nextWeekTasks() == null ? null : request.nextWeekTasks())
                .blockers(request.blockers() == null ? null : request.blockers())
                .hoursWorked(request.hoursWorked() == null ? null : request.hoursWorked())
                .status(ReportStatus.PENDING)
                .user(user)
                .project(project)
                .build();

        Report saved = reportRepository.save(report);

        return ReportMapper.convertToResponseDto(saved);
    }

    @Override
    @Transactional
    public ReportResponse updateReport(Long id, UpdateReportRequest request, Principal principal) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + id));

        User loggedInUser = (User) ((Authentication) principal).getPrincipal();

        if (!report.getUser().getId().equals(loggedInUser.getId())) {
            throw new AccessDeniedException("You are not allowed to update this resource");
        }

        boolean isRequestingSubmission = request.isSubmitted() != null && request.isSubmitted();

        if (isRequestingSubmission) {
            if (isAnyFieldEmpty(request, report)) {
                throw new IllegalArgumentException("Cannot submit report with incomplete information. " +
                        "Tasks completed, next week tasks, blockers, and hours worked are all mandatory upon submission.");
            }
        }

        if (request.tasksCompleted() != null && !request.tasksCompleted().isBlank()) {
            report.setTasksCompleted(request.tasksCompleted());
        }
        if (request.nextWeekTasks() != null && !request.nextWeekTasks().isBlank()) {
            report.setNextWeekTasks(request.nextWeekTasks());
        }
        if (request.blockers() != null && !request.blockers().isBlank()) {
            report.setBlockers(request.blockers());
        }
        if (request.hoursWorked() != null) {
            report.setHoursWorked(request.hoursWorked());
        }

        if (request.isSubmitted() != null && report.getSubmittedAt() == null) {
            final boolean before = report.getWeekEndDate().before(new Date());
            if (Boolean.TRUE.equals(request.isSubmitted())) {
                report.setStatus(before ? ReportStatus.LATE : ReportStatus.SUBMITTED);
                report.setSubmittedAt(Instant.now());
            } else {
                report.setStatus(before ? ReportStatus.LATE : ReportStatus.PENDING);
                report.setSubmittedAt(null);
            }
        }

        Report updatedReport = reportRepository.save(report);
        return ReportMapper.convertToResponseDto(updatedReport);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReportResponse> getReports(Long userId, Long projectId, String weekCode,
                                           String reportStatus, Pageable pageable, Principal principal) {

        final boolean isManager = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getAuthorities().stream()
                .anyMatch(grantedAuthority -> Objects.equals(grantedAuthority.getAuthority(), "ROLE_MANAGER"));

        User loggedInUser = (User) ((Authentication) principal).getPrincipal();

        if (userId == null && projectId == null && weekCode == null && reportStatus == null) {
            Page<Report> reportPage = reportRepository.findAllByUserId(loggedInUser.getId(), pageable);
            return reportPage.map(ReportMapper::convertToResponseDto);
        }

        if (!isManager) {
            userId = loggedInUser.getId();
        }

        List<Specification<Report>> specs = new ArrayList<>();

        if (userId != null) {
            Long finalUserId = userId;
            specs.add((root, query, cb) -> cb.equal(root.get("user").get("id"), finalUserId));
        }
        if (projectId != null) {
            specs.add((root, query, cb) -> cb.equal(root.get("project").get("id"), projectId));
        }
        if (weekCode != null && !weekCode.isBlank()) {
            specs.add((root, query, cb) -> cb.equal(root.get("weekCode"), weekCode));
        }
        if (reportStatus != null && !reportStatus.isBlank()) {
            specs.add((root, query, cb) -> cb.equal(root.get("status"), parseStatus(reportStatus)));
        }

        Specification<Report> spec = Specification.allOf(specs);

        Page<Report> reportPage = reportRepository.findAll(spec, pageable);
        return reportPage.map(ReportMapper::convertToResponseDto);
    }

    @Override
    public List<String> getAllWeekCodes() {
        return reportRepository.findAllDistinctWeekCodes();
    }

    private ReportStatus parseStatus(String status) {
        return Arrays.stream(ReportStatus.values())
                .filter(s -> s.name().equalsIgnoreCase(status))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid report status: " + status));
    }

    private boolean isAnyFieldEmpty(UpdateReportRequest request, Report existingReport) {
        String tasks = request.tasksCompleted() != null ? request.tasksCompleted() : existingReport.getTasksCompleted();
        String nextTasks = request.nextWeekTasks() != null ? request.nextWeekTasks() : existingReport.getNextWeekTasks();
        String blockers = request.blockers() != null ? request.blockers() : existingReport.getBlockers();
        Integer hours = request.hoursWorked() != null ? request.hoursWorked() : existingReport.getHoursWorked();

        return (tasks == null || tasks.isBlank()) ||
                (nextTasks == null || nextTasks.isBlank()) ||
                (blockers == null || blockers.isBlank()) ||
                (hours == null);
    }
}
