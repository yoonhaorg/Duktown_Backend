package com.duktown.domain.unit.controller;

import com.duktown.domain.cleaningUnit.entity.CleaningUnitInitDB;
import com.duktown.domain.unit.service.UnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("unit")
public class UnitController {

    //삭제해삭제삭제
    private final CleaningUnitInitDB unitService;

    @GetMapping("/{userId}")
    public void unitAllocation(@PathVariable Long userId){
        unitService.allocationCleaning(userId);
    }

}
