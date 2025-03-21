package com.reportai.www.reportapi.api.v1.outlets.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateOutletRequestDTO {
    private String name;

    private String address;

    private String postalCode;

    private String contactNumber;

    @Email
    private String email;
    
    private String description;
}
