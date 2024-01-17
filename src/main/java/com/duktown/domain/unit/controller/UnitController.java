package com.duktown.domain.unit.controller;

import com.duktown.domain.unit.service.UnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("unit")
public class UnitController {

    private final UnitService unitService;

//    @GetMapping("/{userId}")
//    public void unitAllocation(@PathVariable Long userId){
//        unitService.assignRoom(userId);
//    }

}
