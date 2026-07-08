package com.sisencodigital.dashboard.repository;

import com.sisencodigital.dashboard.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT DISTINCT p FROM Project p " +
            "LEFT JOIN FETCH p.teamMembers " +
            "WHERE p.id IN (SELECT p2.id FROM Project p2 JOIN p2.teamMembers tm WHERE tm.id = :userId)")
    List<Project> findByTeamMembersContaining(@Param("userId") Long userId);

    // Fetch all projects and eagerly load their team members in a single query
    @Query("SELECT DISTINCT p FROM Project p LEFT JOIN FETCH p.teamMembers")
    List<Project> findAllWithTeamMembers();

    // Fetch a single project by ID with its team members pre-loaded
    @Query("SELECT p FROM Project p LEFT JOIN FETCH p.teamMembers WHERE p.id = :id")
    Optional<Project> findByIdWithTeamMembers(@Param("id") Long id);
}
