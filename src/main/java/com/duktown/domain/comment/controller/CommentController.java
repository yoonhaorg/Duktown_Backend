package com.duktown.domain.comment.controller;

import com.duktown.domain.comment.dto.CommentDto;
import com.duktown.domain.comment.service.CommentService;
import com.duktown.global.security.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> createComment(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody CommentDto.CreateRequest request
    ) {
        commentService.createComment(customUserDetails.getId(), request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<CommentDto.ListResponse> getCommentList(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(value = "deliveryId", required = false) Long deliveryId,
            @RequestParam(value = "dailyId", required = false) Long dailyId,
            @RequestParam(value = "marketId", required = false) Long marketId
    ) {
        return ResponseEntity.ok(
                commentService.getCommentList(customUserDetails.getId(), deliveryId, dailyId, marketId)
        );
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<Void> updateComment(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentDto.UpdateRequest request
    ) {
        commentService.updateComment(customUserDetails.getId(), commentId, request);
        return ResponseEntity.ok().build();
    }
}
