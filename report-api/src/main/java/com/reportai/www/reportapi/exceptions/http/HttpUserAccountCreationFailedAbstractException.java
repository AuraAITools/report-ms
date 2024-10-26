package com.reportai.www.reportapi.exceptions.http;

import org.springframework.http.HttpStatus;


public class HttpUserAccountCreationFailedAbstractException extends HttpAbstractException {
    protected static final int code = 409002;
    protected static final HttpStatus httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
    protected static final String target = "";

    public HttpUserAccountCreationFailedAbstractException(String message, String target) {
        super(httpStatus, code, message, target);
    }
}
