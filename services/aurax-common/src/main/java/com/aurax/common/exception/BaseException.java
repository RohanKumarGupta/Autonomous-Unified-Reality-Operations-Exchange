package com.aurax.common.exception;

import org.springframework.http.HttpStatus;

public abstract class BaseException extends RuntimeException {
    private final String errorCode;
    private final HttpStatus status;

    public BaseException(String message, String errorCode, HttpStatus status) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }

    public BaseException(String message, Throwable cause, String errorCode, HttpStatus status) {
        super(message, cause);
        this.errorCode = errorCode;
        this.status = status;
    }

    public BaseException(String errorCode, int statusCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.status = HttpStatus.valueOf(statusCode);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
