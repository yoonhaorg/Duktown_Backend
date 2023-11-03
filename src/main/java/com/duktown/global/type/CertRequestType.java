package com.duktown.global.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CertRequestType {
    DORM_STUDENT(0, "ROLE_DORM_STUDENT", "기숙사생 인증"),
    DORM_COUNCIL(1, "ROLE_DORM_COUNCIL", "사생회 인증"),
    MANAGER(2, "ROLE_MANGER", "관리자 인증");

    private final int value;
    private final String roleKey;
    private final String description;
}
