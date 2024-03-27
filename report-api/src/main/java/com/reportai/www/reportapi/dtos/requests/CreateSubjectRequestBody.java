package com.reportai.www.reportapi.dtos.requests;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.reportai.www.reportapi.dtos.DTOSupport;
import com.reportai.www.reportapi.entities.User;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.UUID;

@Getter
@Setter
public class CreateSubjectRequestBody extends DTOSupport<User> {

    @JsonProperty("institution_id")
    private UUID institutionId;
    private String name;

    public CreateSubjectRequestBody(ModelMapper modelMapper, UUID institutionId, String name) {
        super(modelMapper);
        this.institutionId = institutionId;
        this.name = name;
    }

    //TODO: implement
    @Override
    public User toEntity() {
        return null;
    }
}
