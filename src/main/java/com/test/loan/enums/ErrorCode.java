package com.test.loan.enums;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // 4xx – Client errors
    VALIDATION_FAILED("USR_001", HttpStatus.BAD_REQUEST, "Validation failed"),
    USER_NOT_FOUND("USR_404", HttpStatus.NOT_FOUND, "User not found"),
    DATA_CONFLICT("USR_409", HttpStatus.CONFLICT, "Data conflict"),

    // 5xx – Server errors
    INTERNAL_SERVER_ERROR("SYS_500", HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");

    private final String code;
    private final HttpStatus status;
    private final String defaultMessage;

    ErrorCode(String code, HttpStatus status, String defaultMessage) {
        this.code = code;
        this.status = status;
        this.defaultMessage = defaultMessage;
    }

    public String getCode() {
        return code;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
