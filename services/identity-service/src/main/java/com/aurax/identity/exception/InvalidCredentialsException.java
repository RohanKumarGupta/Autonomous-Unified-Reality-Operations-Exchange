package com.aurax.identity.exception;

import com.aurax.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends BaseException {
    public InvalidCredentialsException() {
        super("INVALID_CREDENTIALS", HttpStatus.UNAUTHORIZED.value(), "Invalid email or password");
    }
}
