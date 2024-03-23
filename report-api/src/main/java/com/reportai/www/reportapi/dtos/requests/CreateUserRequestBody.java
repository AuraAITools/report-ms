package com.reportai.www.reportapi.dtos.requests;

import com.reportai.www.reportapi.entities.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateUserRequestBody implements DTOSupport<User> {

    private String email;

    private String role;

    private String name;

    @Override
    public DTOSupport<User> toDTO(User entity) {
        return null;
    }

    @Override
    public User toEntity() {
        return User.builder()
                .name(this.getName())
                .role(this.getRole())
                .email(this.getEmail())
                .build();
    }
}
