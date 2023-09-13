package com.duktown.domain.user.controller;

import com.duktown.domain.user.dto.UserDto;
import com.duktown.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // TODO: ResponseDTO 생성해서 반환하도록 변경
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody final UserDto.SignupRequest signupRequest) {
        return ResponseEntity.ok().build();
    }
}
