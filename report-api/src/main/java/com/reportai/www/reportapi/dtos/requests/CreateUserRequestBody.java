package com.reportai.www.reportapi.dtos.requests;

import com.reportai.www.reportapi.dtos.DTO;
import com.reportai.www.reportapi.dtos.DTOSupport;
import com.reportai.www.reportapi.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.core.ParameterizedTypeReference;

@Getter
@Setter
public class CreateUserRequestBody extends DTOSupport<User> {

    private String email;

    private String role;

    private String name;

    public CreateUserRequestBody(ModelMapper modelMapper, String email, String role, String name) {
        super(modelMapper);
        this.email = email;
        this.role = role;
        this.name = name;
    }

    @Override
    public User toEntity() {
        return User.builder()
                .name(this.getName())
                .email(this.getEmail())
                .build();
    }
}
