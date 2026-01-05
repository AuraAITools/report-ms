package com.aura.details.keycloak.client.exceptions;

public class UserAlreadyTaggedToTenantException extends RuntimeException {
    public UserAlreadyTaggedToTenantException(String message) {
        super(message);
    }
}
