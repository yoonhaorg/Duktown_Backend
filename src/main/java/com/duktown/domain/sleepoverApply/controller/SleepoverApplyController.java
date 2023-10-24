package com.duktown.domain.sleepoverApply.controller;

import com.duktown.domain.sleepoverApply.dto.SleepoverApplyDto;
import com.duktown.domain.sleepoverApply.service.SleepoverApplyService;
import com.duktown.global.security.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
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
         return ResponseEntity.ok().build();
    }


    @GetMapping("/{sleepoverId}")
    public ResponseEntity<SleepoverApplyDto.ResponseGetSleepoverApply> getList(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long sleepoverId
    ){
        return ResponseEntity.ok(sleepoverApplyService.getSleepoverApply(sleepoverId));
    }

    @PatchMapping("/{sleepoverApplyId}")
    public  ResponseEntity<?> updateList(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long sleepoverApplyId
    ){
        sleepoverApplyService.approveSleepoverApply(sleepoverApplyId);
        return ResponseEntity.ok().build();
    }

}
