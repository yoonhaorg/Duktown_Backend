package com.duktown.domain.replaceApply.controller;

import com.duktown.domain.replaceApply.dto.ReplaceApplyDto;
import com.duktown.domain.replaceApply.service.ReplaceApplyService;
import com.duktown.global.security.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("replaceApply")
@RequiredArgsConstructor
public class ReplaceApplyController {
    private final ReplaceApplyService replaceApplyService;

    @PostMapping()
    public ResponseEntity<Void> createRepairApply(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody ReplaceApplyDto.ReplaceApplyRequestDto request)
    {
        replaceApplyService.createReplaceApply(customUserDetails.getId(),request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
