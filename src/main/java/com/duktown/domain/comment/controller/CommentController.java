package com.duktown.domain.comment.controller;

import com.duktown.domain.comment.dto.CommentDto;
import com.duktown.domain.comment.service.CommentService;
import com.duktown.global.security.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping()
    public ResponseEntity<Void> createComment(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody CommentDto.CreateRequest request
    ) {
        commentService.createComment(customUserDetails.getId(), request);
        return ResponseEntity.ok().build();
    }
}
