package com.duktown.global.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserType {
    USER(0, "일반 사용자"),
    DORM_STUDENT(1, "기숙사생"),
    DORM_COUNCIL(2, "사생회"),
    MANAGER(3, "관리자");

    private final int value;
    private final String description;
}
