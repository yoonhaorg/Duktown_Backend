package com.duktown.domain.dormCert.dto;

import com.duktown.domain.dormCert.entity.DormCert;
import com.duktown.domain.user.entity.User;
import com.duktown.global.type.CertRequestType;
import com.duktown.global.type.HallName;
import com.duktown.global.type.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class DormCertDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserCertRequest{
        // image는 requestParam MultipartFile로 입력받음

        @NotNull(message = "인증요청 타입은 필수 값입니다.")
        @Min(value = 0, message = "인증요청 타입 값은 0부터 2 사이의 값이어야 합니다.")
        @Max(value = 2, message = "인증요청 타입 값은 0부터 2 사이의 값이어야 합니다.")
        private Integer certRequestType;

        @NotBlank(message = "학번은 필수 값입니다.")
        private String studentId;

        @NotNull(message = "기숙사 관명은 필수 값입니다.")
        private Integer hallName;

        @NotNull(message = "기숙사 층은 필수 값입니다.")
        private Integer floorNumber;

        @NotNull(message = "기숙사 동은 필수 값입니다.")
        private Integer buildingNumber;

        @NotNull(message = "기숙사 호수는 필수 값입니다.")
        private Integer roomNumber;

        public DormCert toEntity(User user, CertRequestType certRequestType, HallName hallName, String imgUrl) {
            // 최초 등록 시 certified는 null
            return DormCert.builder()
                    .user(user)
                    .certRequestType(certRequestType)
                    .imgUrl(imgUrl)
                    .studentId(this.studentId)
                    .hallName(hallName)
                    .floorNumber(this.floorNumber)
                    .buildingNumber(this.buildingNumber)
                    .roomNumber(this.roomNumber)
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    public static class CertResponse {
        private Boolean certified;
        private RoleType resultRoleType;
    }
}