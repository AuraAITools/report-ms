package com.reportai.www.reportapi.api.v1.outlets.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateOutletRequestDTO {
    @NotEmpty
    private String name;

    @NotEmpty
    private String address;

    @NotEmpty
    private String postalCode;

    @NotEmpty
    private String contactNumber;

    @Email
    private String email;

    private String description;
}
