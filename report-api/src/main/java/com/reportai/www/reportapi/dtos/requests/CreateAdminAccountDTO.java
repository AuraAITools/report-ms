package com.reportai.www.reportapi.dtos.requests;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
public class CreateAdminAccountDTO {

    @Email(message = "email is in invalid format")
    @NotEmpty(message = "email must not be empty")
    public String email;

    @NotEmpty(message = "first name cannot be empty")
    public String firstName;

    @NotEmpty(message = "last name cannot be empty")
    public String lastName;

}
