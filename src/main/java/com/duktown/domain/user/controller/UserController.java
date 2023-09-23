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
        return ResponseEntity.ok(userService.emailCheck(emailCheckRequest));
    }

    @PostMapping("/id-duplicate")
    public ResponseEntity<UserDto.IdCheckResponse> idCheck(@Valid @RequestBody final UserDto.IdCheckRequest idCheckRequest) {
        return ResponseEntity.ok(userService.idCheck(idCheckRequest));
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto.SignUpResponse> signup(@Valid @RequestBody final UserDto.SignupRequest signupRequest) {
        return ResponseEntity.ok(userService.signup(signupRequest));
    }
}
