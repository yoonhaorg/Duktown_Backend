package com.duktown.global.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{

    private final CustomErrorType errorType;
    private final int code;
    private final String errorMessage;

    // Without Cause Exception
    public CustomException(CustomErrorType customErrorType) {
        super(customErrorType.getErrorMessage());
        this.errorType = customErrorType;
        this.code = customErrorType.getCode();
        this.errorMessage = customErrorType.getErrorMessage();
    }

    public CustomException(CustomErrorType customErrorType, String errorMessage) {
        super(errorMessage);
        this.errorType = customErrorType;
        this.code = errorType.getCode();
        this.errorMessage = errorMessage;
    }

    // With Cause Exception
    public CustomException(CustomErrorType customErrorType, Exception exception) {
        super(customErrorType.getErrorMessage(), exception);
        this.errorType = customErrorType;
        this.code = errorType.getCode();
        this.errorMessage = customErrorType.getErrorMessage();
    }

    public CustomException(CustomErrorType customErrorType, String errorMessage, Exception exception) {
        super(errorMessage, exception);
        this.errorType = customErrorType;
        this.code = customErrorType.getCode();
        this.errorMessage = errorMessage;
    }
}
