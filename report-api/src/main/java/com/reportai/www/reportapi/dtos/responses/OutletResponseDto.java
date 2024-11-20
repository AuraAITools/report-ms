package com.reportai.www.reportapi.dtos.responses;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OutletResponseDto {
    @NotEmpty
    private String name;

    @NotEmpty
    private String address;

    @NotEmpty
    private String postalCode;

    @NotEmpty
    private String contactNumber;
}
