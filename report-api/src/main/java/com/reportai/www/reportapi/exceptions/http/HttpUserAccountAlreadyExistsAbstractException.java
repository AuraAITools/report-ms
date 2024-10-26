package com.reportai.www.reportapi.exceptions.http;

import org.springframework.http.HttpStatus;


public class HttpUserAccountAlreadyExistsAbstractException extends HttpAbstractException {
    protected static final int code = 409002;
    protected static final HttpStatus httpStatus = HttpStatus.CONFLICT;
    protected static final String target = "";

    public HttpUserAccountAlreadyExistsAbstractException(String message, String target) {
        super(httpStatus, code, message, target);
    }
}
