package com.aura.app.http.graphql.v1.institutions.exceptions;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException() {
    }

    public AccessDeniedException(String message) {
        super(message);
    }
}
