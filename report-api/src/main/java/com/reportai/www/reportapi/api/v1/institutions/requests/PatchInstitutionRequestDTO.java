package com.reportai.www.reportapi.api.v1.institutions.requests;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;


import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(NON_EMPTY)
public class PatchInstitutionRequestDTO {

    private String name;

    private String email;

    private String uen;

    private String address;

    private String contactNumber;
}
