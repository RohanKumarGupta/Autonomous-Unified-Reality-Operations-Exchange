package com.aurax.identity.exception;

import com.aurax.common.exception.DuplicateResourceException;

public class EmailAlreadyExistsException extends DuplicateResourceException {
    public EmailAlreadyExistsException(String email) {
        super("User", "email", email);
    }
}
