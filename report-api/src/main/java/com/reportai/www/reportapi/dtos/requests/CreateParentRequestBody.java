package com.reportai.www.reportapi.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.reportai.www.reportapi.dtos.DTO;
import com.reportai.www.reportapi.entities.Parent;
import com.reportai.www.reportapi.entities.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class CreateParentRequestBody implements DTO<Parent> {


    private String name;

    @JsonProperty("user")
    @NotNull(message = "user may not be null")
    private CreateUserRequestBody user;

    @Override
    public Parent toEntity() {
        return Parent.builder()
                .name(name)
                .user(user.toEntity())
                .build();
    }

    @Override
    public <R extends DTO<Parent>> R of(Parent entity) {
        return null;
    }
}
