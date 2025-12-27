package com.aura.core.auth;

public class AuthorizationValidatorImpl implements AuthorizationValidator {

    public AuthorizationValidatorImpl() {
    }

    @Override
    public void validateAuthorization(AuthorizationStrategy authorizationStrategy) {
        authorizationStrategy.checkAuthorization();
    }
}
