package com.test.loan.dto.common;

import java.time.LocalDateTime;
import java.util.List;

public final class ApiErrorResponse {

    private final LocalDateTime timestamp;
    private final int status;
    private final String errorCode;
    private final String message;
    private final String path;
    private final List<FieldErrorResponse> errors;

    private ApiErrorResponse(LocalDateTime timestamp, int status, String errorCode, String message, String path, List<FieldErrorResponse> errors) {
        this.timestamp = timestamp;
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
        this.path = path;
        this.errors = errors;
    }

    public static ApiErrorResponse of(LocalDateTime timestamp, int status, String errorCode, String message, String path, List<FieldErrorResponse> errors) {
        return new ApiErrorResponse(timestamp, status, errorCode, message, path, errors);
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    public List<FieldErrorResponse> getErrors() {
        return errors;
    }
}
