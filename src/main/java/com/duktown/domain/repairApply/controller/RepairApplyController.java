package com.duktown.domain.repairApply.controller;

import com.duktown.domain.repairApply.dto.RepairApplyDto;
import com.duktown.domain.repairApply.service.RepairApplyService;
import com.duktown.global.security.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/repairApply")
@RequiredArgsConstructor
public class RepairApplyController {

    private final RepairApplyService repairApplyService;

    @PostMapping
    public ResponseEntity<Void> create(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody RepairApplyDto.RepairApplyRequest request){
        repairApplyService.saveApply(customUserDetails.getId(), request);
        return ResponseEntity.ok().build();
    }

    //TODO: hallName 탭 삭제, 목록 조건으로 올해 년도만 제한 + 확인 + 해결 여부 추가
    @GetMapping
    public ResponseEntity<RepairApplyDto.RepairApplyListResponse> getApplyList(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(value = "hallName") Integer hallName
    ){
        return ResponseEntity.ok().body(
                repairApplyService.getApplyList(customUserDetails.getId(), hallName));
    }

    @GetMapping("/{repairApplyId}")
    public ResponseEntity<RepairApplyDto.RepairApplyResponse> getApply(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("repairApplyId") Long repairApplyId){
        return ResponseEntity.ok().body(repairApplyService.getApply(customUserDetails.getId(), repairApplyId));
    }

    @PutMapping("/{repairApplyId}")
    public ResponseEntity<RepairApplyDto> update(
            @Valid @RequestBody RepairApplyDto.RepairApplyRequest request,
            @PathVariable("repairApplyId") Long repairApplyId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails){
        repairApplyService.updateApply(customUserDetails.getId(), repairApplyId, request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/check/{repairApplyId}")
    public ResponseEntity<RepairApplyDto> check(
            @PathVariable("repairApplyId") Long repairApplyId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails){
        repairApplyService.check(customUserDetails.getId(), repairApplyId);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/solve/{repairApplyId}")
    public ResponseEntity<RepairApplyDto> solve(
            @PathVariable("repairApplyId") Long repairApplyId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails){
        repairApplyService.solve(customUserDetails.getId(), repairApplyId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{repairApplyId}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("repairApplyId") Long repairApplyId){
        repairApplyService.deleteApply(customUserDetails.getId(), repairApplyId);
        return ResponseEntity.ok().build();
    }
    
    
}
