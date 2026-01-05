package com.aura.app.exceptions;

/**
 * BusinessException are expected in the business flow
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
