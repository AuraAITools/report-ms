package com.reportai.www.reportapi.dtos.responses.commons;

import com.reportai.www.reportapi.dtos.DTOSupport;
import com.reportai.www.reportapi.entities.User;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class UserDTO extends DTOSupport<User> {
    private UUID Id;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private String email;

    private String role;

    private String name;

    public UserDTO(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    public User toEntity() {
        return null;
    }
}
