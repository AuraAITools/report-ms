package com.aura.app.exceptions;

/**
 * TechnicalException are unexpected in the business flow and require further investigation
 */
public class TechnicalException extends RuntimeException {
    public TechnicalException(String message) {
        super(message);
    }
}
