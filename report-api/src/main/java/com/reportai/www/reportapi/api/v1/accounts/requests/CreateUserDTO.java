package com.reportai.www.reportapi.api.v1.accounts.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.reportai.www.reportapi.entities.Account;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
public class CreateUserDTO {

    @Email
    @NotEmpty
    public String email;

    @NotEmpty
    public String firstName;

    @NotEmpty
    public String lastName;

    @NotEmpty
    public String contact;

    //    @NotEmpty
    public Account.RELATIONSHIP relationship;


}
