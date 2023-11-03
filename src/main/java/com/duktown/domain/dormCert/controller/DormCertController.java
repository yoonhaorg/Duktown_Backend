package com.duktown.domain.dormCert.controller;

import com.duktown.domain.dormCert.dto.DormCertDto;
import com.duktown.domain.dormCert.service.DormCertService;
import com.duktown.global.security.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dormCerts")
public class DormCertController {
    private final DormCertService dormCertService;

    @PostMapping
    public ResponseEntity<Void> createDormCert(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestPart(value = "certRequest") DormCertDto.UserCertRequest request,
            @RequestPart (value = "certImg") MultipartFile certImg
    ) {
        dormCertService.createDormCert(customUserDetails.getId(), request, certImg);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/check/{dormCertId}")
    public ResponseEntity<DormCertDto.CertResponse> checkDormCert(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("dormCertId") Long dormCertId,
            @RequestParam("approve") Boolean approve
    ) {
        return ResponseEntity.ok(
                dormCertService.checkDormCert(customUserDetails.getId(), dormCertId, approve)
        );
    }
}