package com.duktown.domain.sleepoverApply.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApprovalType {
// 승인, 대기, 반려
Waiting(0,"대기"),
Refuse(1,"반려"),
Approved(2,"승인");

private final int value;
private final String ApprovalType;

}
