package com.reportai.www.reportapi.api.v1.accounts.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@Builder
public class CreateEducatorResponseDTO {

    @NotEmpty
    private String id;
    
    @NotEmpty
    private String name;

    @Email
    @NotEmpty
    private String email;
}
