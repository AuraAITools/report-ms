package com.reportai.www.reportapi.exceptions.http;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@Setter
public abstract class HttpAbstractException extends RuntimeException {
    protected HttpStatus httpStatus;
    protected int code;
    protected String message;
    protected String target;
}
