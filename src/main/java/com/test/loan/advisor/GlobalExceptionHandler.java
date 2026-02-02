package com.test.loan.advisor;

import com.test.loan.dto.common.ApiErrorResponse;
import com.test.loan.dto.common.ErrorCode;
import com.test.loan.dto.common.FieldErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /* -------------------- Validation Errors  -------------------- */

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        List<FieldErrorResponse> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> new FieldErrorResponse(
                        err.getField(),
                        err.getDefaultMessage()))
                .collect(Collectors.toList());

        ErrorCode ec = ErrorCode.VALIDATION_FAILED;

        return ResponseEntity
                .status(ec.getStatus())
                .body(ApiErrorResponse.of(
                        LocalDateTime.now(),
                        ec.getStatus().value(),
                        ec.getCode(),
                        "Validation failed",
                        request.getRequestURI(),
                        errors
                ));
    }

    /* -------------------- Business Errors  -------------------- */

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorResponse> handleBusiness(
            BusinessException ex,
            HttpServletRequest request) {

        ErrorCode ec = ex.getErrorCode();

        String message = ex.getMessage() != null
                ? ex.getMessage()
                : "Business rule violation";

        return ResponseEntity
                .status(ec.getStatus())
                .body(ApiErrorResponse.of(
                        LocalDateTime.now(),
                        ec.getStatus().value(),
                        ec.getCode(),
                        message,
                        request.getRequestURI(),
                        null
                ));
    }

    /* -------------------- Database Constraint Errors (409) -------------------- */

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrity(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {

        ErrorCode ec = ErrorCode.DATA_CONFLICT;

        return ResponseEntity
                .status(ec.getStatus())
                .body(ApiErrorResponse.of(
                        LocalDateTime.now(),
                        ec.getStatus().value(),
                        ec.getCode(),
                        "Data conflict occurred",
                        request.getRequestURI(),
                        null
                ));
    }

    /* -------------------- Not Found (404) -------------------- */

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNoResourceFound(
            NoResourceFoundException ex,
            HttpServletRequest request) {

        ErrorCode ec = ErrorCode.RESOURCE_NOT_FOUND;

        return ResponseEntity
                .status(ec.getStatus())
                .body(ApiErrorResponse.of(
                        LocalDateTime.now(),
                        ec.getStatus().value(),
                        ec.getCode(),
                        "Requested resource not found",
                        request.getRequestURI(),
                        null
                ));
    }

    /* -------------------- Method Not Allowed (405) -------------------- */

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodNotAllowed(
            HttpRequestMethodNotSupportedException ex,
            HttpServletRequest request) {

        ErrorCode ec = ErrorCode.METHOD_NOT_ALLOWED;

        return ResponseEntity
                .status(ec.getStatus())
                .body(ApiErrorResponse.of(
                        LocalDateTime.now(),
                        ec.getStatus().value(),
                        ec.getCode(),
                        "HTTP method not supported for this endpoint",
                        request.getRequestURI(),
                        null
                ));
    }

    /* -------------------- Unsupported Media Type (415) -------------------- */

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> handleMediaType(
            HttpMediaTypeNotSupportedException ex,
            HttpServletRequest request) {

        ErrorCode ec = ErrorCode.UNSUPPORTED_MEDIA_TYPE;

        return ResponseEntity
                .status(ec.getStatus())
                .body(ApiErrorResponse.of(
                        LocalDateTime.now(),
                        ec.getStatus().value(),
                        ec.getCode(),
                        "Unsupported media type",
                        request.getRequestURI(),
                        null
                ));
    }

    /* -------------------- Fallback (500) -------------------- */

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpected(
            Exception ex,
            HttpServletRequest request) {

        ex.printStackTrace();

        ErrorCode ec = ErrorCode.INTERNAL_SERVER_ERROR;
        System.out.println("\n\n\n\n\n\n\n\n\nException:"+ ex.getMessage());

        return ResponseEntity
                .status(ec.getStatus())
                .body(ApiErrorResponse.of(
                        LocalDateTime.now(),
                        ec.getStatus().value(),
                        ec.getCode(),
                        "Unexpected internal server error",
                        request.getRequestURI(),
                        null
                ));
    }

}
