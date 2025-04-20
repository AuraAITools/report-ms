package com.reportai.www.reportapi.api.v1.materials.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateMaterialRequestDTO {
    @NotEmpty
    private String id;

    @NotEmpty
    private String name;

    // TODO: optional file urlencoded string
    private String file;
}
