package com.reportai.www.reportapi.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.reportai.www.reportapi.dtos.DTO;
import com.reportai.www.reportapi.entities.Institution;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateInstitutionRequestBody implements DTO<Institution> {

    private String name;

    @JsonProperty("user")
    @NotNull(message = "user may not be null")
    private CreateUserRequestBody user;

    @Override
    public Institution toEntity() {
        return Institution.builder().name(name).user(user.toEntity()).build();
    }

    @Override
    public <R extends DTO<Institution>> R of(Institution entity) {
        return null;
    }
}
