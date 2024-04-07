package com.reportai.www.reportapi.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.reportai.www.reportapi.dtos.DTOSupport;
import com.reportai.www.reportapi.entities.Parent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.UUID;

@Getter
@Setter
public class CreateParentRequestBody extends DTOSupport<Parent> {

    @JsonProperty("institution_id")
    private UUID institutionId;
    private CreateUserRequestBody user;

    public CreateParentRequestBody(ModelMapper modelMapper, UUID institutionId, CreateUserRequestBody user) {
        super(modelMapper);
        this.institutionId = institutionId;
        this.user = user;
    }

    @Override
    public Parent toEntity() {
        return Parent
                .builder()
                .build();
    }
}
