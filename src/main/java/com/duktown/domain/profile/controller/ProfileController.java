package com.duktown.domain.profile.controller;

import com.duktown.domain.delivery.dto.DeliveryDto;
import com.duktown.domain.post.dto.PostDto;
import com.duktown.domain.profile.dto.ProfileDto;
import com.duktown.domain.profile.service.ProfileService;
import com.duktown.global.security.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/my")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping()
    public ResponseEntity<ProfileDto.ProfileResponse> getMyProfile(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return ResponseEntity.ok(profileService.getMyProfile(customUserDetails.getId()));
    }

    @GetMapping("/delivery")
    public ResponseEntity<DeliveryDto.DeliveryListResponse> getMyDeliveries(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(value = "sortBy", required = false) Integer sortBy
    ) {
        return ResponseEntity.ok(
                profileService.getMyDeliveries(customUserDetails.getId(), sortBy)
        );
    }

    @GetMapping("/posts")
    public ResponseEntity<PostDto.PostListResponse> getMyPosts(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(value = "category") Integer category
    ){
        return ResponseEntity.ok(
                profileService.getMyPosts(customUserDetails.getId(), category)
        );
    }

    @GetMapping("/delivery/commented")
    public ResponseEntity<DeliveryDto.DeliveryListResponse> getMyCommentedDeliveries(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(value = "sortBy", required = false) Integer sortBy
    ) {
        return ResponseEntity.ok(
                profileService.getMyCommentedDeliveries(customUserDetails.getId(), sortBy)
        );
    }

    @GetMapping("/posts/commented")
    public ResponseEntity<PostDto.PostListResponse> getMyCommentedPosts(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(value = "category") Integer category
    ) {
        return ResponseEntity.ok(
                profileService.getMyCommentedPosts(customUserDetails.getId(), category)
        );
    }
}
