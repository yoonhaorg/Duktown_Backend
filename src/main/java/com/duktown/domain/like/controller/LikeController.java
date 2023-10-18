package com.duktown.domain.like.controller;

import com.duktown.domain.like.service.LikeService;
import com.duktown.global.security.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/likes")
public class LikeController {

    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<Void> createLike(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(value = "deliveryId", required = false) Long deliveryId,
            @RequestParam(value = "dailyId", required = false) Long dailyId,
            @RequestParam(value = "marketId", required = false) Long marketId,
            @RequestParam(value = "commentId", required = false) Long commentId
    ) {
        likeService.createLike(customUserDetails.getId(), deliveryId, dailyId, marketId, commentId);
        return ResponseEntity.ok().build();
    }
}
