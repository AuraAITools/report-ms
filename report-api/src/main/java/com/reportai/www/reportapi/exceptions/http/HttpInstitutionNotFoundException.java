package com.reportai.www.reportapi.exceptions.http;

import org.springframework.http.HttpStatus;

public class HttpInstitutionNotFoundException extends HttpAbstractException {
    protected static final int code = 404001;
    protected static final String target = "institution";
    protected static final String message = "institution does not exists";
    protected static final HttpStatus httpStatus = HttpStatus.NOT_FOUND;

    public HttpInstitutionNotFoundException() {
        super(httpStatus, code, message, target);
    }
}
