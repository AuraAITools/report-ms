package com.reportai.www.reportapi.exceptions.http;

import org.springframework.http.HttpStatus;


public class HttpInternalServerAbstractException extends HttpAbstractException {
    protected static final int code = 500001;
    protected static final HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    protected static final String target = "";

    public HttpInternalServerAbstractException(String message, String target) {
        super(httpStatus, code, message, target);
    }

    public HttpInternalServerAbstractException() {
        super(httpStatus, code, "an unexpected error has occured", target);
    }
}
