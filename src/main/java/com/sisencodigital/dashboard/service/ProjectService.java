package com.sisencodigital.dashboard.service;

import com.sisencodigital.dashboard.dto.request.CreateProjectRequest;
import com.sisencodigital.dashboard.dto.request.UpdateProjectRequest;
import com.sisencodigital.dashboard.dto.response.ProjectResponse;

import java.util.List;

public interface ProjectService {
    ProjectResponse createProject (CreateProjectRequest request);
    ProjectResponse updateProject (Long id, UpdateProjectRequest request);
    List<ProjectResponse> getProjects(Long id, Long userId);
    void deleteProject(Long id);
}
