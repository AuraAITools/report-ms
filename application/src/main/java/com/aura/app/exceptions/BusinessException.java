package com.aura.app.exceptions;

import lombok.Getter;

/**
 * Expected exceptions in the business flow, handled by the user or frontend.
 */
@Getter
public class BusinessException extends RuntimeException {
    private final int httpStatus;
    private final String errorCode;

    public BusinessException(String message, int httpStatus, String errorCode) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }
}
