package com.duktown.global.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    DAILY(0, "일상"),
    MARKET(1, "장터");

    private final int value;
    private final String category;
}
