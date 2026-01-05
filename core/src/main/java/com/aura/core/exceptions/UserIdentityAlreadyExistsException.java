package com.aura.core.exceptions;

public class UserIdentityAlreadyExistsException extends RuntimeException {
    public UserIdentityAlreadyExistsException(String message) {
        super(message);
    }
}
