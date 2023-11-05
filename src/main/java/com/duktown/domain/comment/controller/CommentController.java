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
            @RequestParam(value = "postId", required = false) Long postId
    ) {

        return ResponseEntity.ok(
                commentService.getCommentList(customUserDetails.getId(), deliveryId, postId)
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

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long commentId
    ) {
        commentService.deleteComment(customUserDetails.getId(), commentId);
        return ResponseEntity.ok().build();
    }
}
