package com.reportai.www.reportapi.dtos.responses;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    @JsonProperty
    private String message;

    @JsonProperty
    private String target;

    @JsonProperty("error_code")
    private String errorCode;
}
