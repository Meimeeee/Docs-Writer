package com.docsWriter.api.exception;

import com.docsWriter.api.enums.ErrorCode;
import com.docsWriter.api.utils.BaseResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class ControllerAdvisor {

    private <T> ResponseEntity<BaseResponse<T>> build(
            int status,
            boolean success,
            String message,
            T data,
            Map<String, List<String>> fieldErrors
    ) {
        BaseResponse<T> body = new BaseResponse<>(
                status,
                success,
                message,
                data,
                fieldErrors,
                LocalDateTime.now()
        );
        return ResponseEntity.status(status)
                .contentType(MediaType.APPLICATION_JSON) //always return JSON type
                .body(body);
    }

    // ───────── 1. AppException (lỗi custom của bạn) ─────────
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<BaseResponse<Object>> handleAppException(CustomException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        log.warn("AppException: {} - {}", errorCode.name(), ex.getMessage());

        return build(
                errorCode.getStatus().value(),
                false,
                ex.getMessage(),
                null,
                null
        );
    }

    // ───────── 2. Lỗi validate body (@Valid, @NotBlank...) ─────────
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, List<String>> fieldErrors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(err -> {
            fieldErrors
                    .computeIfAbsent(err.getField(), k -> new ArrayList<>())
                    .add(err.getDefaultMessage());
        });

        log.warn("Validation error: {}", fieldErrors);

        return build(
                ErrorCode.VALIDATION_ERROR.getStatus().value(),
                false,
                "Validation error",
                null,
                fieldErrors
        );
    }

    // ───────── 3. Lỗi validate param (@RequestParam, @PathVariable) ─────────
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BaseResponse<Object>> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, List<String>> fieldErrors = new HashMap<>();

        ex.getConstraintViolations().forEach(v -> {
            String field = v.getPropertyPath().toString();
            fieldErrors
                    .computeIfAbsent(field, k -> new ArrayList<>())
                    .add(v.getMessage());
        });

        log.warn("Constraint violation: {}", fieldErrors);

        return build(
                ErrorCode.VALIDATION_ERROR.getStatus().value(),
                false,
                "Validation error",
                null,
                fieldErrors
        );
    }

    // ───────── 4. Bị security chặn (403) ─────────
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<BaseResponse<Object>> handleAccessDenied(AccessDeniedException ex) {
        log.warn("Access denied: {}", ex.getMessage());

        return build(
                ErrorCode.ACCESS_DENIED.getStatus().value(),
                false,
                ErrorCode.ACCESS_DENIED.getDefaultMessage(),
                null,
                null
        );
    }

    // ───────── 5. Sai method: gọi POST cho endpoint chỉ cho GET ─────────
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<BaseResponse<Object>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        log.warn("Method not allowed: {}", ex.getMessage());

        return build(
                ErrorCode.METHOD_NOT_ALLOWED.getStatus().value(),
                false,
                ErrorCode.METHOD_NOT_ALLOWED.getDefaultMessage(),
                null,
                null
        );
    }

    // ───────── 6. Các lỗi còn lại (500) ─────────
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Object>> handleOtherException(Exception ex) {
        log.error("Unexpected error", ex);

        return build(
                ErrorCode.INTERNAL_ERROR.getStatus().value(),
                false,
                ErrorCode.INTERNAL_ERROR.getDefaultMessage(),
                null,
                null
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BaseResponse<Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity
                .badRequest()
                .body(BaseResponse.failure(400, ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Object>> handleUnexpected(Exception ex) {
        // log ex
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(BaseResponse.failure(500, "Internal server error"));
    }

}
