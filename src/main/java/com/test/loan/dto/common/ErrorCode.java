package com.test.loan.dto.common;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    VALIDATION_FAILED("USER_001", HttpStatus.BAD_REQUEST),
    DATA_CONFLICT("RES_409", HttpStatus.CONFLICT),
    USER_NOT_FOUND("USER_404", HttpStatus.NOT_FOUND),
    CUSTOMER_NOT_FOUND("CUST_404", HttpStatus.NOT_FOUND),
    LOAN_ALREADY_EXISTS("LOAN_409", HttpStatus.CONFLICT),
    LOAN_NOT_FOUND("LOAN_404", HttpStatus.NOT_FOUND),
    UNPROCESSABLE_CONTENT("RES_422", HttpStatus.UNPROCESSABLE_ENTITY),
    RESOURCE_NOT_FOUND("RES_404", HttpStatus.NOT_FOUND),
    METHOD_NOT_ALLOWED("RES_405", HttpStatus.METHOD_NOT_ALLOWED),
    UNSUPPORTED_MEDIA_TYPE("RES_415", HttpStatus.UNSUPPORTED_MEDIA_TYPE),
    INTERNAL_SERVER_ERROR("SYS_500", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final HttpStatus status;

    ErrorCode(String code, HttpStatus status) {
        this.code = code;
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
