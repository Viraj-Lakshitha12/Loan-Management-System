package com.test.loan.dto.common;

import java.time.LocalDateTime;

public final class ApiResponse<T> {

    private final LocalDateTime timestamp;
    private final int status;
    private final String message;
    private final T data;

    private ApiResponse(LocalDateTime timestamp,
                        int status,
                        String message,
                        T data) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(int status, String message, T data) {
        return new ApiResponse<>(
                LocalDateTime.now(),
                status,
                message,
                data
        );
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public int getStatus() { return status; }
    public String getMessage() { return message; }
    public T getData() { return data; }
}
