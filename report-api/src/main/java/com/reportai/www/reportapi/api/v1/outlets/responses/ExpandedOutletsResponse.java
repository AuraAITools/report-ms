package com.reportai.www.reportapi.api.v1.outlets.responses;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.reportai.www.reportapi.api.v1.accounts.responses.CreateEducatorResponseDTO;
import com.reportai.www.reportapi.api.v1.accounts.responses.CreateStudentResponseDTO;
import com.reportai.www.reportapi.api.v1.courses.responses.CreateCourseDTOResponseDTO;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ExpandedOutletsResponse {
    @NotEmpty
    private String id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String address;

    @NotEmpty
    private String postalCode;

    @NotEmpty
    private String contactNumber;

    private String description;


    private List<CreateCourseDTOResponseDTO> courses;
    private List<CreateEducatorResponseDTO> educators;
    private List<CreateStudentResponseDTO> students;
}
