package com.duktown.domain.repairApply.service;

import com.duktown.domain.repairApply.entity.RepairApply;

import java.util.List;

public interface RepairApplyService {

    RepairApply saveApply(RepairApply apply);

    int updateApply(RepairApply apply);

    int deleteApply(Long id);

    List<RepairApply> findAll();

    List<RepairApply> findByUser(Long id);

    RepairApply findById(Long id);

    int checkApply(RepairApply apply);

    int solveApply(RepairApply apply);

}
