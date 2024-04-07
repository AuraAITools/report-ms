package com.reportai.www.reportapi.dtos.requests;


import com.reportai.www.reportapi.dtos.DTO;
import com.reportai.www.reportapi.entities.User;
import jakarta.transaction.NotSupportedException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class CreateUserRequestBody implements DTO<User> {

    @NotEmpty(message = "email is a mandatory field")
    private String email;

    @NotEmpty(message = "name is a mandatory field")
    private String name;

    @Override
    public User toEntity() {
        return User.builder()
                .email(email)
                .name(name)
                .build();
    }

    // FIXME: currently this method does not need to be supported
    @Override
    public <R extends DTO<User>> R of(User entity) {
        return null;
    }
}
