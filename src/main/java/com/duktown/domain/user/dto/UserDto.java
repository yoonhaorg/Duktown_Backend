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
        @NotBlank(message = "이메일은 필수 값입니다.")
        @Email(regexp = "^[a-zA-Z0-9.][^@]*@duksung.ac.kr$",
                message = "이메일 형식이 올바르지 않습니다. 덕성 이메일을 입력해주세요.")
        private String email;

        @NotBlank(message = "아이디는 필수 값입니다.")
        private String loginId;

        @NotBlank(message = "비밀번호는 필수 값입니다.")
        private String password;

        public User toEntity(String encodedPassword){
            return User.builder()
                    .email(this.email)
                    .loginId(this.loginId)
                    .password(encodedPassword)
                    .roleType(RoleType.USER)
                    .build();
        }
    }

    // 아이디 중복 체크 요청
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IdCheckRequest {
        @NotBlank(message = "아이디는 필수 값입니다.")
        private String loginId;
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

    // 아이디 체크 응답
    @Getter
    @AllArgsConstructor
    public static class IdCheckResponse {
        private Boolean isDuplicated;
    }

    // 이메일 제공 요청 (비밀번호 찾기에서 사용)
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmailRequest {
        @NotBlank(message = "이메일은 필수 값입니다.")
        @Email(regexp = "^[a-zA-Z0-9.][^@]*@duksung.ac.kr$",
                message = "이메일 형식이 올바르지 않습니다. 덕성 이메일을 입력해주세요.")
        private String email;
    }
}
