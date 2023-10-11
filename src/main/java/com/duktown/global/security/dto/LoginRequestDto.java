package com.duktown.global.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LoginRequestDto {
    @NotBlank(message = "아이디는 필수 값입니다.")
    private String loginId;
    @NotBlank(message = "비밀번호는 필수 값입니다.")
    private String password;
}
