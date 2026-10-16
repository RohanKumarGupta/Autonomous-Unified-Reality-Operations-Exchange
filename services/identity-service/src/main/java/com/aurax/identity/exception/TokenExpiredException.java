package com.aurax.identity.exception;

import com.aurax.common.exception.UnauthorizedException;

public class TokenExpiredException extends UnauthorizedException {
    public TokenExpiredException() {
        super("TOKEN_EXPIRED", "Refresh token has expired");
    }
}
