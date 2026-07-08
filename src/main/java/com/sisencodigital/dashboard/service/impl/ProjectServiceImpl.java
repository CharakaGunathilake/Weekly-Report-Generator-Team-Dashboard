package com.sisencodigital.dashboard.service.impl;

import com.sisencodigital.dashboard.dto.request.CreateProjectRequest;
import com.sisencodigital.dashboard.dto.request.UpdateProjectRequest;
import com.sisencodigital.dashboard.dto.response.ProjectResponse;
import com.sisencodigital.dashboard.entity.Project;
import com.sisencodigital.dashboard.entity.User;
import com.sisencodigital.dashboard.exceptions.custom.ResourceNotFoundException;
import com.sisencodigital.dashboard.repository.ProjectRepository;
import com.sisencodigital.dashboard.repository.UserRepository;
import com.sisencodigital.dashboard.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ProjectResponse createProject(CreateProjectRequest request) {
        Set<User> users = new HashSet<>();

        if (request.userIds() != null && !request.userIds().isEmpty()) {
            users = new HashSet<>(userRepository.findAllById(request.userIds()));

            if (users.size() != request.userIds().size()) {
                throw new IllegalArgumentException("One or more user IDs are invalid");
            }
        }

        Project project = Project.builder()
                .name(request.name().trim())
                .description(request.description())
                .teamMembers(users)
                .build();

        Project savedProject = projectRepository.save(project);

        return new ProjectResponse(
                savedProject.getId(),
                savedProject.getName(),
                savedProject.getDescription(),
                savedProject.getTeamMembers().stream().map(User::getName).toList()
        );
    }

    @Override
    @Transactional
    public ProjectResponse updateProject(Long id, UpdateProjectRequest request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

        if (request.userIds() != null) {
            Set<User> users = Set.copyOf(userRepository.findAllById(request.userIds()));
            project.getTeamMembers().clear();
            project.getTeamMembers().addAll(users);
        }

        if (request.name() != null && !request.name().isBlank()) {
            project.setName(request.name().trim());
        }

        if (request.description() != null && !request.description().isBlank()) {
            project.setDescription(request.description());
        }

        Project updatedProject = projectRepository.save(project);
        return new ProjectResponse(
                updatedProject.getId(),
                updatedProject.getName(),
                updatedProject.getDescription(),
                updatedProject.getTeamMembers().stream().map(User::getName).toList()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponse> getProjects(Long id, Long userId) {
        boolean isManager = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_MANAGER"));

        if (!isManager && userId != null) {
            throw new AccessDeniedException("Access denied: Only managers can filter by userId");
        }

        if (isManager && userId != null) {
            List<Project> projects = projectRepository.findByTeamMembersContaining(userId);
            return projects.stream()
                    .map(project -> new ProjectResponse(
                            project.getId(),
                            project.getName(),
                            project.getDescription(),
                            project.getTeamMembers().stream().map(User::getName).toList()
                    ))
                    .toList();
        }

        if (id != null) {
            Project project = projectRepository.findByIdWithTeamMembers(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
            return List.of(new ProjectResponse(
                    project.getId(),
                    project.getName(),
                    project.getDescription(),
                    project.getTeamMembers().stream().map(User::getName).toList()
            ));
        } else {
            List<Project> projects = projectRepository.findAllWithTeamMembers();
            return projects.stream()
                    .map(project -> new ProjectResponse(
                            project.getId(),
                            project.getName(),
                            project.getDescription(),
                            project.getTeamMembers().stream().map(User::getName).toList()
                    ))
                    .toList();
        }
    }

    @Override
    @Transactional
    public void deleteProject(Long projectId) {
        projectRepository.deleteById(projectId);
    }
}