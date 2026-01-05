package com.aura.core.exceptions;

public class UserIdentityAlreadyTaggedToTenantException extends RuntimeException {
    public UserIdentityAlreadyTaggedToTenantException(String message) {
        super(message);
    }
}
