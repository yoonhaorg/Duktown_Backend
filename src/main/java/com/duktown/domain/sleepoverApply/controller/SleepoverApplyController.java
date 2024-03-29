package com.duktown.domain.sleepoverApply.controller;

import com.duktown.domain.sleepoverApply.dto.SleepoverApplyDto;
import com.duktown.domain.sleepoverApply.service.SleepoverApplyService;
import com.duktown.global.security.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("sleepoverApply")
public class SleepoverApplyController {
    private final SleepoverApplyService sleepoverApplyService;

    @PostMapping()
    public ResponseEntity<?> createSleepoverApply(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody SleepoverApplyDto.RequestSleepoverApplyDto request){
         sleepoverApplyService.createSleepoverApply(customUserDetails.getId(),request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("")
    public ResponseEntity<?> getTotalAvailablePeriod(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        return ResponseEntity.ok(sleepoverApplyService.totalAvailablePeriod(customUserDetails.getId()));
    }

    @GetMapping("/student")
    public ResponseEntity<SleepoverApplyDto.ResponseGetSleepoverApplyFromStudent> getSleepoverList(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(required = false, defaultValue = "1", value = "pageNo") int pageNo
    ){
        return ResponseEntity.ok( sleepoverApplyService.getListSleepoverApply(customUserDetails.getId(),pageNo));
    }

    @GetMapping("/manager")
    public ResponseEntity<SleepoverApplyDto.ResponseGetSleepoverApplyFromManager> getSleepoverListFromManager(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(required = false, defaultValue = "1", value = "pageNo") int pageNo
    ){
        return ResponseEntity.ok( sleepoverApplyService.getListSleepoverApply(pageNo));
    }

    // 상세 조회
    @GetMapping("/{sleepoverId}")
    public ResponseEntity<SleepoverApplyDto.ResponseGetSleepoverApply> getSleepoverDetail(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long sleepoverId
    ){
        return ResponseEntity.ok(sleepoverApplyService.getDetailSleepoverApply(sleepoverId));
    }

    // 관리자 승인
    @PatchMapping("/{sleepoverApplyId}")
    public  ResponseEntity<?> updateList(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long sleepoverApplyId
    ){
        sleepoverApplyService.approveSleepoverApply(sleepoverApplyId);
        System.out.println(customUserDetails.getAuthorities());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{sleepoverId}")
    public ResponseEntity<Void> deleteSleepoverApply(
            @PathVariable Long sleepoverId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        sleepoverApplyService.deleteSleepoverApply(sleepoverId,customUserDetails.getId());
        return ResponseEntity.ok().build();
    }

}
