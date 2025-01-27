package com.reportai.www.reportapi.advices;

import com.reportai.www.reportapi.dtos.responses.HttpErrorResponseDTO;
import com.reportai.www.reportapi.dtos.responses.HttpMultipleErrorResponseDTO;
import com.reportai.www.reportapi.exceptions.http.HttpAbstractException;
import com.reportai.www.reportapi.exceptions.http.HttpMultiAbstractException;
import com.reportai.www.reportapi.mappers.ExceptionMappers;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionAdvice {


    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionAdvice.class);

    @ExceptionHandler(HttpAbstractException.class)
    public ResponseEntity<HttpErrorResponseDTO> handleDefinedSingleHttpExceptions(HttpAbstractException exception) {
        log.error(Arrays.toString(exception.getStackTrace()));
        HttpErrorResponseDTO httpErrorResponseDTO = ExceptionMappers.convert(exception);
        return new ResponseEntity<>(httpErrorResponseDTO, exception.getHttpStatus());
    }

    /**
     * @param exceptions
     * @return
     */
    @ExceptionHandler(HttpMultiAbstractException.class)
    public ResponseEntity<HttpMultipleErrorResponseDTO> handleDefinedMultiHttpExceptions(HttpMultiAbstractException exceptions) {
        log.error(Arrays.toString(exceptions.getStackTrace()));
        HttpMultipleErrorResponseDTO httpMultipleErrorResponseDTO = ExceptionMappers.convert(exceptions.getHttpAbstractExceptions());
        return new ResponseEntity<>(httpMultipleErrorResponseDTO, exceptions.getHttpStatus());
    }

    /**
     * catch all unexpected errors and returns errpr response
     *
     * @param exception
     * @return
     */
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<HttpErrorResponseDTO> handleAllOtherExceptions(Exception exception) {
//        log.error(Arrays.toString(exception.getStackTrace()));
//        HttpInternalServerAbstractException httpInternalServerException = new HttpInternalServerAbstractException("an unexpected server error has occurred", "");
//        HttpErrorResponseDTO internalServiceErrorDTO = ExceptionMappers.convert(httpInternalServerException);
//        return new ResponseEntity<>(internalServiceErrorDTO, httpInternalServerException.getHttpStatus());
//    }
}
