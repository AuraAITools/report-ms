package com.reportai.www.reportapi.api.exceptions;

import com.reportai.www.reportapi.dtos.responses.ErrorResponse;
import com.reportai.www.reportapi.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import static java.util.Objects.isNull;

// TODO: create a ErrorMessage DTO
@ControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFoundException(Exception e) {
        ErrorResponse notFoundErrorResponse = ErrorResponse.builder()
                .message(e.getMessage())
                .errorCode("404001")
                .build();
        return new ResponseEntity<>(notFoundErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleValidationError(MethodArgumentNotValidException e) {
        ErrorResponse validationErrorResponse = ErrorResponse.builder()
                .message(e.getMessage())
                .errorCode("400001")
                .target(isNull(e.getTarget()) ? null : e.getTarget().toString())
                .build();
        return new ResponseEntity<>(validationErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NoResourceFoundException.class})
    public ResponseEntity<ErrorResponse> handlerNoRestResourceFoundException(NoResourceFoundException e) {
        ErrorResponse noRestResourceFoundErrorResponse = ErrorResponse.builder()
                .message(e.getMessage())
                .errorCode("404002")
                .target(e.getResourcePath())
                .build();
        return new ResponseEntity<>(noRestResourceFoundErrorResponse, HttpStatus.NOT_FOUND);
    }
}
