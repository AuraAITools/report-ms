package com.reportai.www.reportapi.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.reportai.www.reportapi.dtos.DTOSupport;
import com.reportai.www.reportapi.entities.Educator;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.UUID;

@Getter
@Setter
public class CreateEducatorRequestBody extends DTOSupport<Educator> {
    @JsonProperty("institution_id")
    private UUID institutionId;
    private CreateUserRequestBody user;

    public CreateEducatorRequestBody(ModelMapper modelMapper, UUID institutionId, CreateUserRequestBody user) {
        super(modelMapper);
        this.institutionId = institutionId;
        this.user = user;
    }

    // TODO: implement
    @Override
    public Educator toEntity() {
        return null;
    }
}
