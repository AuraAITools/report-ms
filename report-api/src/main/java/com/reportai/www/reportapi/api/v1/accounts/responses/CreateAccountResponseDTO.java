package com.reportai.www.reportapi.api.v1.accounts.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.reportai.www.reportapi.entities.Account;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@Builder
public class CreateAccountResponseDTO {

    @NotEmpty
    public String id;

    @Email
    @NotEmpty
    public String email;

    @NotEmpty
    public String firstName;

    @NotEmpty
    public String lastName;

    @NotEmpty
    public String contact;

    public Account.RELATIONSHIP relationship;

}
