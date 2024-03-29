package com.duktown.domain.repairApply.entity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RepairApplyRepository extends JpaRepository<RepairApply, Long> {

    @Query("SELECT e FROM RepairApply e WHERE YEAR(e.createdAt) = YEAR(CURRENT_DATE)")
    Page<RepairApply> findAllByCreatedAtThisYear(Pageable pageable);

    @Query("SELECT r FROM RepairApply r WHERE r.checked = false AND r.createdAt <= :threeDaysAgo")
    List<RepairApply> findByCheckedAndCreatedAtAfterThreeDays(@Param("threeDaysAgo") LocalDateTime threeDaysAgo);
}
