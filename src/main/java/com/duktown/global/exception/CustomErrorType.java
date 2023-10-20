package com.duktown.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum CustomErrorType {

    // TODO ErrorCode를 추가해주세요

    // General (1xxxx)
    INVALID_HTTP_METHOD(METHOD_NOT_ALLOWED, 10001, "잘못된 Http Method 요청입니다."),
    INVALID_VALUE(BAD_REQUEST, 10002, "잘못된 입력값입니다."),
    SERVER_INTERNAL_ERROR(INTERNAL_SERVER_ERROR, 10003, "서버 내부에 오류가 발생했습니다."),

    // Token & Authentication & Authorization (2xxxx)
    ACCESS_DENIED(FORBIDDEN, 20001, "접근 권한이 없습니다."),
    AUTHENTICATION_REQUIRED(UNAUTHORIZED, 20002, "인증이 필요한 요청입니다."),
    TOKEN_NOT_EXIST(BAD_REQUEST, 20003, "JWT Token이 존재하지 않습니다."),
    INVALID_TOKEN(BAD_REQUEST, 20004, "유효하지 않은 JWT Token입니다."),
    ACCESS_TOKEN_EXPIRED(BAD_REQUEST, 20005, "만료된 Access Token입니다."),
    UNHANDLED_TOKEN_ERROR(UNAUTHORIZED, 20006, "알 수 없는 Token 에러가 발생했습니다."),  // TODO: 추후 자연스럽게 수정

    // User (3xxxx)
    LOGIN_FAILED(UNAUTHORIZED, 30001, "이메일 또는 비밀번호가 일치하지 않습니다."),
    USER_NOT_FOUND(NOT_FOUND, 30002, "사용자를 찾을 수 없습니다."),
    EMAIL_ALREADY_EXIST(CONFLICT, 30003, "이미 존재하는 이메일입니다."),
    LOGIN_ID_ALREADY_EXIST(CONFLICT, 30004, "이미 존재하는 아이디입니다."),
    HAVE_NO_PERMISSION(FORBIDDEN, 30005, "수정 및 삭제에 대한 권한이 없습니다."),

    // Delivery (4xxxx)
    DELIVERY_NOT_FOUND(NOT_FOUND, 40001, "존재하지 않는 배달팟입니다."),

    // Daily (5xxxx)
    DAILY_NOT_FOUND(NOT_FOUND, 50001, "존재하지 않는 일상글입니다."),

    // Market(6xxxx)
    MARKET_NOT_FOUND(NOT_FOUND, 60001, "존재하지 않는 장터글입니다."),

    // Comment(7xxxx)
    COMMENT_NOT_FOUND(NOT_FOUND, 70001, "존재하지 않는 댓글입니다."),
    PARENT_COMMENT_NOT_FOUND(NOT_FOUND, 70002, "존재하지 않는 상위 댓글입니다."),
    COMMENT_TARGET_NOT_SELECTED(BAD_REQUEST, 70003, "댓글을 생성하거나 조회할 대상이 선택되지 않았습니다."),
    COMMENT_DEPTH_ERROR(BAD_REQUEST, 70004, "대댓글에 대댓글을 달 수 없습니다."),
    COMMENT_TARGET_ERROR(BAD_REQUEST, 70005, "댓글을 달 대상은 하나만 선택할 수 있습니다."),

    // Like(8xxxx)
    LIKE_TARGET_NOT_SELECTED(BAD_REQUEST, 80001, "좋아요할 대상이 선택되지 않았습니다."),
    LIKE_TARGET_ERROR(BAD_REQUEST, 80002, "좋아요할 대상은 하나만 선택할 수 있습니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String errorMessage;

    public int getStatusCode() {
        return this.httpStatus.value();
    }
}
