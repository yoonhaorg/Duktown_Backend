package com.duktown.global.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HallName {
    INTERNATIONAL(0, "국제기숙사"),
    GAON1(1, "가온 1관"),
    GAON2(2, "가온 2관");

    private final int value;
    private final String hallName;
}
