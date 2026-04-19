package com.aura.app.exceptions;

import lombok.Getter;

/**
 * Unexpected backend exceptions that require technical personnel to rectify.
 */
@Getter
public class TechnicalException extends RuntimeException {
    private final int httpStatus;
    private final String errorCode;

    public TechnicalException(String message) {
        this(message, 500, "INTERNAL_ERROR");
    }

    public TechnicalException(String message, int httpStatus, String errorCode) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }
}
