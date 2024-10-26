package com.reportai.www.reportapi.clients.keycloak.exceptions;

public class KeycloakUserAccountAlreadyExistsException extends RuntimeException{
    public KeycloakUserAccountAlreadyExistsException(String message) {
        super(message);
    }
}
