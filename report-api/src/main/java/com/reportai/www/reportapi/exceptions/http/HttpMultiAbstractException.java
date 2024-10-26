package com.reportai.www.reportapi.exceptions.http;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class HttpMultiAbstractException extends RuntimeException{
    protected HttpStatus httpStatus;
    protected int code;
    protected String message;
    protected String target;
    protected List<HttpAbstractException> httpAbstractExceptions;
}
