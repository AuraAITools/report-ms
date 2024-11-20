package com.reportai.www.reportapi.dtos.responses;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@Getter
@Setter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(NON_EMPTY)
public class InstitutionResponseDto {

    private UUID id;

    private String name;

    private String email;

    private String uen;

    private String address;

    private String contactNumber;

}
