package com.docsWriter.api.enums;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    EMAIL_ALREADY_IN_USE(HttpStatus.CONFLICT, "Email already in use"),
    USERNAME_ALREADY_IN_USE(HttpStatus.CONFLICT, "Username already in use"),
    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "Account not found"),
    ACCOUNT_INACTIVE(HttpStatus.FORBIDDEN, "Account is inactive"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "Invalid password"),
    PROFILE_NOT_FOUND(HttpStatus.NOT_FOUND, "Profile not found"),

    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "Validation error"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "Access denied"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "Method not allowed"),

    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),

    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid Google token");

    private final HttpStatus status;
    private final String defaultMessage;

    ErrorCode(HttpStatus status, String defaultMessage) {
        this.status = status;
        this.defaultMessage = defaultMessage;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

}
