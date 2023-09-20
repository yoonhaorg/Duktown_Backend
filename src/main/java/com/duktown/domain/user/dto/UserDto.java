package com.duktown.domain.user.dto;

import com.duktown.domain.user.entity.User;
import com.duktown.global.type.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

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
        @NotEmpty(message = "이메일은 필수적으로 입력해야 합니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        @Pattern(regexp = "[A-Za-z0-9+_.-]+@duksung.ac.kr", message = "덕성 메일을 입력해주세요.")
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
