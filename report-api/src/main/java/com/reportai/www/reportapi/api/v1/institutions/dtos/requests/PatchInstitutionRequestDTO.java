package com.reportai.www.reportapi.api.v1.institutions.dtos.requests;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PatchInstitutionRequestDTO {

    private String name;

    private String email;

    private String uen;

    private String address;

    private String contactNumber;
}
