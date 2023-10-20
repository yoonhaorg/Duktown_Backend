package com.duktown.domain.like.controller;

import com.duktown.domain.like.dto.LikeDto;
import com.duktown.domain.like.service.LikeService;
import com.duktown.global.security.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/likes")
public class LikeController {

    private final LikeService likeService;

    // 좋아요 추가, 취소
    @PostMapping
    public ResponseEntity<LikeDto.LikeResponse> like(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody LikeDto.LikeRequest request
    ) {
        return ResponseEntity.ok(
                likeService.like(customUserDetails.getId(), request)
        );
    }
}
