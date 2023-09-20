package com.duktown.domain.user.controller;

import com.duktown.domain.user.dto.UserDto;
import com.duktown.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/email-duplicate")
    public ResponseEntity<UserDto.EmailCheckResponse> emailCheck(@Valid @RequestBody final UserDto.EmailCheckRequest emailCheckRequest) {
        return ResponseEntity.ok(userService.emailDuplicateCheck(emailCheckRequest));
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto.SignUpResponse> signup(@RequestBody final UserDto.SignupRequest signupRequest) {
        return ResponseEntity.ok(userService.signup(signupRequest));
    }
}
