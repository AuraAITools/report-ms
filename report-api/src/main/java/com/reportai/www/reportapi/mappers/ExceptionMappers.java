package com.reportai.www.reportapi.mappers;

import com.reportai.www.reportapi.dtos.responses.HttpErrorResponseDTO;
import com.reportai.www.reportapi.dtos.responses.HttpMultipleErrorResponseDTO;
import com.reportai.www.reportapi.exceptions.http.HttpAbstractException;

import java.util.List;
import static java.util.stream.Collectors.toList;

public class ExceptionMappers {
    public static HttpErrorResponseDTO convert(HttpAbstractException from) {
        return HttpErrorResponseDTO
                .builder()
                .code(from.getCode())
                .target(from.getTarget())
                .message(from.getMessage())
                .build();
    }

    /**
     * converts a list of http exceptions to a error response with multiple errors
     * @param from the first element in the list is the main error shown
     * @return
     */
    public static HttpMultipleErrorResponseDTO convert(List<HttpAbstractException> from) {
        assert !from.isEmpty();
        HttpAbstractException mainException = from.getFirst();
        return HttpMultipleErrorResponseDTO
                .builder()
                .message(mainException.getMessage())
                .code(mainException.getCode())
                .target(mainException.getTarget())
                .errors(from.stream().map(ExceptionMappers::convert).collect(toList()))
                .build();
    }

    ;
}
