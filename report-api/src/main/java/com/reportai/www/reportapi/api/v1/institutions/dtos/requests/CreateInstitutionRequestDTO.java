package com.reportai.www.reportapi.api.v1.institutions.dtos.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;


import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@JsonInclude(NON_EMPTY)
public class CreateInstitutionRequestDTO {
    @NotEmpty
    private String name;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private String uen;

    @NotEmpty
    private String address;

    @NotEmpty
    private String contactNumber;

}

