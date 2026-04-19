package com.aura.app.exceptions;

public class UnauthorisedException extends BusinessException {
    public UnauthorisedException(String message) {
        super(message, 401, "UNAUTHORISED");
    }
}
