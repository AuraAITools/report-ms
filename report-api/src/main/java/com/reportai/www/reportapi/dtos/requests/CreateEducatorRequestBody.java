package com.reportai.www.reportapi.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.reportai.www.reportapi.dtos.DTO;
import com.reportai.www.reportapi.entities.Educator;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateEducatorRequestBody implements DTO<Educator> {

    private String name;

    @JsonProperty("user")
    @NotNull(message = "user may not be null")
    private CreateUserRequestBody user;

    @Override
    public Educator toEntity() {
        return Educator.builder()
                .name(name)
                .user(user.toEntity())
                .build();
    }

    @Override
    public <R extends DTO<Educator>> R of(Educator entity) {
        return null;
    }

}
