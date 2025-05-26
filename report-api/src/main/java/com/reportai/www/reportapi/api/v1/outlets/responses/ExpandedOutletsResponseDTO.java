package com.reportai.www.reportapi.api.v1.outlets.responses;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.reportai.www.reportapi.api.v1.accounts.responses.EducatorResponseDTO;
import com.reportai.www.reportapi.api.v1.courses.responses.CourseResponseDTO;
import com.reportai.www.reportapi.api.v1.students.responses.StudentResponseDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@AllArgsConstructor
public class ExpandedOutletsResponseDTO {
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

    @Email
    @NotEmpty
    private String email;

    private List<CourseResponseDTO> courses;
    private List<EducatorResponseDTO> educators;
    private List<StudentResponseDTO> students;
}
