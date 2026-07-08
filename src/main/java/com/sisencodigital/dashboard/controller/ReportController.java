package com.sisencodigital.dashboard.controller;

import com.sisencodigital.dashboard.dto.request.CreateReportRequest;
import com.sisencodigital.dashboard.dto.request.UpdateReportRequest;
import com.sisencodigital.dashboard.dto.response.ReportResponse;
import com.sisencodigital.dashboard.service.ReportService;
import com.sisencodigital.dashboard.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reports")
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    @PreAuthorize("hasAnyRole('TEAM_MEMBER', 'MANAGER')")
    public ApiResponse<ReportResponse> createReport(@RequestBody @Valid CreateReportRequest request, Principal principal) {
        return ApiResponse.success(
                201,
                String.format("Project '%s' created successfully", request.weekCode()),
                reportService.createReport(request, principal)
        );
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('TEAM_MEMBER', 'MANAGER')")
    public ApiResponse<ReportResponse> updateReport(@RequestParam Long id, @RequestBody @Valid UpdateReportRequest request, Principal principal) {
        return ApiResponse.success(
                200,
                "Project updated successfully",
                reportService.updateReport(id, request, principal)
        );
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('TEAM_MEMBER', 'MANAGER')")
    public ApiResponse<Page<ReportResponse>> getReports(@RequestParam(required = false) Long userId,
                                                        @RequestParam(required = false) Long projectId,
                                                        @RequestParam(required = false) String weekCode,
                                                        @RequestParam(required = false) String reportStatus,
                                                        @PageableDefault(sort = "weekStartDate", direction = Sort.Direction.ASC) Pageable pageable,
                                                        Principal principal) {
        return ApiResponse.success(
                200,
                "Projects retrieved successfully",
                reportService.getReports(userId, projectId, weekCode, reportStatus, pageable, principal)
        );
    }

    @GetMapping("/week-codes")
    @PreAuthorize("hasAnyRole('TEAM_MEMBER', 'MANAGER')")
    public ApiResponse<List<String>> getAllWeekCodes() {
        return ApiResponse.success(
                200,
                "Week codes retrieved successfully",
                reportService.getAllWeekCodes()
        );
    }
}
