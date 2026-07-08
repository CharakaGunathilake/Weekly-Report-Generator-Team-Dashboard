package com.sisencodigital.dashboard.controller;

import com.sisencodigital.dashboard.dto.request.CreateProjectRequest;
import com.sisencodigital.dashboard.dto.request.UpdateProjectRequest;
import com.sisencodigital.dashboard.dto.response.ProjectResponse;
import com.sisencodigital.dashboard.service.ProjectService;
import com.sisencodigital.dashboard.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/projects")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ApiResponse<ProjectResponse> createProject(@RequestBody @Valid CreateProjectRequest request) {
        return ApiResponse.success(
                201,
                String.format("Project '%s' created successfully", request.name()),
                projectService.createProject(request)
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ApiResponse<ProjectResponse> updateProject(@PathVariable Long id, @RequestBody @Valid UpdateProjectRequest request) {
        return ApiResponse.success(
                200,
                "Project updated successfully",
                projectService.updateProject(id, request)
        );
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'TEAM_MEMBER')")
    public ApiResponse<List<ProjectResponse>> getProjects(@RequestParam(required = false) Long id, @RequestParam(required = false) Long userId) {
        List<ProjectResponse> projects = projectService.getProjects(id, userId);
        return ApiResponse.success(
                200,
                projects.size() + " Project(s) retrieved successfully",
                projects
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ApiResponse<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ApiResponse.success(
                200,
                "Project deleted successfully",
                null
        );
    }
}
