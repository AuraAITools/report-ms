package com.reportai.www.reportapi.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.reportai.www.reportapi.entities.Class;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class CreateClassRequestBody implements DTOSupport<Class> {

    @JsonProperty("institution_id")
    private UUID institutionId;
    private String name;

    // TODO: write these methods
    @Override
    public DTOSupport<Class> toDTO(Class entity) {
        return null;
    }

    @Override
    public Class toEntity() {
        return Class
                .builder()
                .name(this.getName())
                .build();
    }
}
