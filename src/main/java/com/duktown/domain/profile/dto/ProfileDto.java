package com.duktown.domain.profile.dto;

import com.duktown.domain.user.entity.User;
import com.duktown.global.type.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class ProfileDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ProfileResponse {
        private String name;
        private String email;
//        private HallName hallName;
//        private Integer buildingNumber;
//        private Integer roomNumber;
        private RoleType roleType;

        //TODO: 유닛정보 추가
        public static ProfileResponse from(User user) { //, Unit unit
            return ProfileResponse.builder()
                    .name(user.getName())
                    .email(user.getEmail())
//                    .hallName(unit.getHallName())
//                    .buildingNumber(unit.getBuildingNumber())
//                    .roomNumber(unit.getRoomNumber())
                    .roleType(user.getRoleType())
                    .build();
        }
    }
}
