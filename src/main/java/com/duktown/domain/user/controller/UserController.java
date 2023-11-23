package com.duktown.domain.user.controller;

import com.duktown.domain.emailCert.dto.EmailCertDto;
import com.duktown.domain.emailCert.service.EmailCertService;
import com.duktown.domain.user.dto.UserDto;
import com.duktown.domain.user.service.UserService;
import com.duktown.global.security.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService userService;
    private final EmailCertService emailCertService;

    @PostMapping("/email")
    public ResponseEntity<EmailCertDto.EmailResponse> emailCert(@Valid @RequestBody final EmailCertDto.EmailRequest request) {
        return ResponseEntity.ok(emailCertService.emailSend(request));
    }

    @PostMapping("/email/cert")
    public ResponseEntity<Void> emailCertCheck(@Valid @RequestBody final EmailCertDto.CertRequest request) {
        emailCertService.emailCert(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/id/duplicate")
    public ResponseEntity<UserDto.IdCheckResponse> idCheck(@Valid @RequestBody final UserDto.IdCheckRequest idCheckRequest) {
        return ResponseEntity.ok(userService.idCheck(idCheckRequest));
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto.SignUpResponse> signup(@Valid @RequestBody final UserDto.SignupRequest signupRequest) {
        return ResponseEntity.ok(userService.signup(signupRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(CustomUserDetails customUserDetails) {
        userService.logout(customUserDetails.getUser().getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/id")
    public ResponseEntity<Void> idFindEmailSend(@Valid @RequestBody final EmailCertDto.EmailRequest request) {
        emailCertService.idFindEmailSend(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/id/find")
    public ResponseEntity<EmailCertDto.LoginIdResponse> idFind(@Valid @RequestBody final EmailCertDto.CertRequest request) {
        return ResponseEntity.ok(emailCertService.idFind(request));
    }

    @PostMapping("/password")
    public ResponseEntity<Void> checkLoginIdExists(@Valid @RequestBody final UserDto.IdCheckRequest request) {
        userService.loginIdExists(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password/reset")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody final UserDto.PwdResetRequest request) {
        userService.pwdReset(request);
        return ResponseEntity.ok().build();
    }
}
