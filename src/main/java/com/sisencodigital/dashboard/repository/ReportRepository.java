package com.sisencodigital.dashboard.repository;

import com.sisencodigital.dashboard.entity.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    boolean existsByUserIdAndProjectIdAndWeekCode(Long userId, Long projectId, String weekCode);

    boolean existsByWeekStartDateAndWeekEndDateAndUserIdAndProjectId(Date weekStartDate, Date weekEndDate, Long userId, Long projectId);

    Page<Report> findAll(Specification<Report> spec, Pageable pageable);

    Page<Report> findAllByUserId(Long userId, Pageable pageable);

    @Query("SELECT DISTINCT r.weekCode FROM Report r ORDER BY r.weekCode DESC")
    List<String> findAllDistinctWeekCodes();
}
