package com.aura.core.exceptions;

public class InstitutionCreationRequestConflictException extends DuplicateResourceException {
    public InstitutionCreationRequestConflictException(String message) {
        super(message);
    }
}
