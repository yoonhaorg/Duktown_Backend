package com.duktown.global.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private final int code;
    private final String errorMessage;

    public ErrorResponse(CustomException e) {
        this.code = e.getCode();
        this.errorMessage = e.getMessage();
    }

    public ErrorResponse(CustomErrorType customErrorType) {
        this.code = customErrorType.getCode();
        this.errorMessage = customErrorType.getErrorMessage();
    }
}
