package com.duktown.domain.profile.controller;

import com.duktown.domain.profile.dto.ProfileDto;
import com.duktown.domain.profile.service.ProfileService;
import com.duktown.global.security.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/my")
    public ResponseEntity<ProfileDto.ProfileResponse> getMyProfile(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return ResponseEntity.ok(profileService.getMyProfile(customUserDetails.getId()));
    }
}
