package com.reportai.www.reportapi.api.v1.outletrooms.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateOutletRoomRequestDTO {
    @NotNull
    private String name;

    private String image;

    @NotNull
    private Integer size;
    
    private String details;
}
