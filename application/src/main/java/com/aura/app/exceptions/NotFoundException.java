package com.aura.app.exceptions;

public class NotFoundException extends BusinessException {
    public NotFoundException(String message) {
        super(message, 404, "NOT_FOUND");
    }
}
