package com.sisencodigital.dashboard.repository;

import com.sisencodigital.dashboard.entity.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT r.status, COUNT(r) FROM Report r WHERE r.weekCode = :weekCode GROUP BY r.status")
    List<Object[]> getStatusCountsByWeek(@Param("weekCode") String weekCode);

    @Query("SELECT COUNT(r) FROM Report r WHERE r.weekCode = :weekCode " +
            "AND r.status = 'SUBMITTED' " +
            "AND r.status = 'LATE' " +
            "AND r.blockers IS NOT NULL " +
            "AND TRIM(r.blockers) != '' " +
            "AND LOWER(r.blockers) != 'n/a'")
    long countOpenBlockersByWeek(@Param("weekCode") String weekCode);

    @Query("SELECT r.weekCode, COUNT(r), SUM(COALESCE(r.hoursWorked, 0)) " +
            "FROM Report r " +
            "WHERE r.status = 'SUBMITTED' " +
            "GROUP BY r.weekCode " +
            "ORDER BY r.weekCode ASC")
    List<Object[]> getHistoricalTrendOverTime();

    @Query("SELECT r.project.name, SUM(COALESCE(r.hoursWorked, 0)), COUNT(r) " +
            "FROM Report r " +
            "WHERE r.weekCode = :weekCode AND r.status = 'SUBMITTED' " +
            "GROUP BY r.project.name")
    List<Object[]> getWorkloadDistributionByProject(@Param("weekCode") String weekCode);
}
