package com.duktown.domain.user.dto;

import com.duktown.domain.user.entity.User;
import com.duktown.global.type.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserDto {

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
}
