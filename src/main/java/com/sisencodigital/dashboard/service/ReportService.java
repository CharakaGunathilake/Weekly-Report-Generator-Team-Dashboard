package com.sisencodigital.dashboard.service;

import com.sisencodigital.dashboard.dto.request.CreateReportRequest;
import com.sisencodigital.dashboard.dto.request.UpdateReportRequest;
import com.sisencodigital.dashboard.dto.response.ReportResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;

public interface ReportService {
    ReportResponse createReport (CreateReportRequest request, Principal principal);
    ReportResponse updateReport (Long id, UpdateReportRequest request, Principal principal);
    Page<ReportResponse> getReports (Long userId, Long projectId, String weekCode, String reportStatus, Pageable pageable, Principal principal);
    List<String> getAllWeekCodes();
}
