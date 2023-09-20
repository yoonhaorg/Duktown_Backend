package com.duktown.domain.user.dto;

import com.duktown.domain.user.entity.User;
import com.duktown.global.type.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UserDto {

    // 회원가입 요청
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SignupRequest {
        private String name;
        private String email;
        private String password;

        public User toEntity(String encodedPassword){
            return User.builder()
                    .name(this.name)
                    .email(this.email)
                    .password(encodedPassword)
                    .roleType(RoleType.USER)
                    .build();
        }
    }

    // 이메일 체크 요청
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EmailCheckRequest {
        private String email;
    }

    // 로그인 응답
    @Getter
    @AllArgsConstructor
    public static class UserTokenResponse {
        private RoleType roleType;
        private String accessToken;
        private String refreshToken;
    }

    // 회원가입 응답 (최초 회원가입시 역할은 무조건 USER, 따라서 반환 x)
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SignUpResponse {
        private String accessToken;
        private String refreshToken;
    }

    // 이메일 체크 응답
    @Getter
    @AllArgsConstructor
    public static class EmailCheckResponse {
        private Boolean isDuplicated;
    }

}
