package com.duktown.domain.post.controller;

import com.duktown.domain.post.dto.PostDto;
import com.duktown.domain.post.service.PostService;
import com.duktown.global.security.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<Void> createPost(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody PostDto.PostRequest request){
        postService.createPost(customUserDetails.getId(), request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<PostDto.PostListResponse> getPostList(
            @RequestParam(value = "category") Integer category
    ){
        return ResponseEntity.ok().body(postService.getPostList(category));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto.PostResponse> getPost(@PathVariable("postId") Long id){
        return ResponseEntity.ok().body(postService.getPost(id));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostDto> updatePost(
            @Valid @RequestBody PostDto.PostRequest request,
            @PathVariable("postId") Long id,
            @AuthenticationPrincipal CustomUserDetails customUserDetails){
        postService.updatePost(customUserDetails.getId(), id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("postId") Long id){
        postService.deletePost(customUserDetails.getId(), id);
        return ResponseEntity.ok().build();
    }

}
