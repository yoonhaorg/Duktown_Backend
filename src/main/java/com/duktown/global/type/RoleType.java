package com.duktown.global.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleType {
    USER("ROLE_USER", "일반 사용자"),
    DORM_STUDENT("ROLE_DORM_STUDENT", "기숙사생"),
    DORM_COUNCIL("ROLE_DORM_COUNCIL", "사생회"),
    MANAGER("ROLE_MANAGER", "관리자");

    private final String key;
    private final String description;
}
