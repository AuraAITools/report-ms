package com.reportai.www.reportapi.dtos.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateOutletDTO {
    @NotEmpty
    private String name;

    @NotEmpty
    private String address;

    @NotEmpty
    private String postalCode;

    @NotEmpty
    private String contactNumber;
}
