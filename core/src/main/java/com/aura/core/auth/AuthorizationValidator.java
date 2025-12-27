package com.aura.core.auth;

public interface AuthorizationValidator {
    void validateAuthorization(AuthorizationStrategy authorizationStrategy);
}
