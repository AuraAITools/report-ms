package com.aura.details.keycloak.client.exceptions;

public class UserNotTaggedToTenantException extends RuntimeException {
    public UserNotTaggedToTenantException(String message) {
        super(message);
    }
}
