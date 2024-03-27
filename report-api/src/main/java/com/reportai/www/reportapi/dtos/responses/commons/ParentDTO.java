package com.reportai.www.reportapi.dtos.responses.commons;


import com.reportai.www.reportapi.dtos.DTOSupport;
import com.reportai.www.reportapi.entities.Parent;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class ParentDTO extends DTOSupport<Parent> {

    private UUID id;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    public ParentDTO(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    public Parent toEntity() {
        throw new UnsupportedOperationException("to entity is unsupported in response DTO");
    }
}
