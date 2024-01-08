package com.duktown.domain.repairApply.entity;

import com.duktown.domain.user.entity.User;
import com.duktown.global.type.HallName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RepairApplyRepository extends JpaRepository<RepairApply, Long> {
    List<RepairApply> findByUser(Long userId);

    @Query("SELECT e FROM RepairApply e WHERE YEAR(e.createdAt) = YEAR(CURRENT_DATE)")
    List<RepairApply> findAllByCreatedAtThisYear();

    @Query("select a from RepairApply a where a.hallName = :hallName")
    List<RepairApply> findAllByHallName(@Param(value = "hallName") HallName findHallName);

}
