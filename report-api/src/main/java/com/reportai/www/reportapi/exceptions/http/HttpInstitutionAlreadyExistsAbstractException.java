package com.reportai.www.reportapi.exceptions.http;

import org.springframework.http.HttpStatus;

public class HttpInstitutionAlreadyExistsAbstractException extends HttpAbstractException {
    protected static final int code = 409001;
    protected static final String target = "";
    protected static final String message = "institution already exists";
    protected static final HttpStatus httpStatus = HttpStatus.CONFLICT;

    public HttpInstitutionAlreadyExistsAbstractException() {
        super(httpStatus, code, message, target);
    }
}
