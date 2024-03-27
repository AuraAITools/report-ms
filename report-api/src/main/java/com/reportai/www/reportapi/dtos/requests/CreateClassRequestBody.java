package com.reportai.www.reportapi.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.reportai.www.reportapi.dtos.DTOSupport;
import com.reportai.www.reportapi.entities.Class;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.UUID;

@Getter
@Setter
public class CreateClassRequestBody extends DTOSupport<Class> {

    @JsonProperty("institution_id")
    private UUID institutionId;

    private String name;

    public CreateClassRequestBody(ModelMapper modelMapper, UUID institutionId, String name) {
        super(modelMapper);
        this.institutionId = institutionId;
        this.name = name;
    }

    @Override
    public Class toEntity() {
        return Class
                .builder()
                .name(this.getName())
                .build();
    }
}
