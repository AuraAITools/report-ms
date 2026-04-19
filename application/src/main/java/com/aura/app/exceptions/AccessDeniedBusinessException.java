package com.aura.app.exceptions;

public class AccessDeniedBusinessException extends BusinessException {
    public AccessDeniedBusinessException(String message) {
        super(message, 403, "ACCESS_DENIED");
    }
}
