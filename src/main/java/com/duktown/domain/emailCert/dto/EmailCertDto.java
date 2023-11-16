package com.duktown.domain.emailCert.dto;

import com.duktown.domain.emailCert.entity.EmailCert;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public class EmailCertDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class EmailRequest {  // 이메일 인증 request
        @NotEmpty(message = "이메일은 필수 값입니다.")
        @Email(regexp = "^[a-zA-Z0-9\\.][^@]+\\@duksung.ac.kr$",
                message = "이메일 형식이 올바르지 않습니다. 덕성 이메일을 입력해주세요.")
        private String email;
    }

    // 이메일 체크 응답
    @Getter
    @AllArgsConstructor
    public static class EmailResponse {
        private Boolean isDuplicated;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CertRequest {   // 인증 번호 확인 request
        @NotEmpty(message = "이메일은 필수 값입니다.")
        @Email(regexp = "^[a-zA-Z0-9\\.][^@]+\\@duksung.ac.kr$",
                message = "이메일 형식이 올바르지 않습니다. 덕성 이메일을 입력해주세요.")
        private String email;
        @NotBlank(message = "인증 코드는 필수 값입니다.")
        private String certCode;

        public EmailCert toEntity() {
            return EmailCert.builder()
                    .email(email)
                    .certCode(certCode)
                    .certified(false)   // 최초 저장시 기본 false
                    .build();
        }
    }

}
