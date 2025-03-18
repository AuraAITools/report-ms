package com.reportai.www.reportapi.api.v1.levels.responses;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.reportai.www.reportapi.api.v1.accounts.responses.CreateEducatorResponseDTO;
import com.reportai.www.reportapi.api.v1.accounts.responses.CreateStudentResponseDTO;
import com.reportai.www.reportapi.api.v1.courses.responses.CreateCourseDTOResponseDTO;
import com.reportai.www.reportapi.api.v1.subjects.responses.SubjectResponseDTO;
import jakarta.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@Builder
public class ExpandedLevelsResponseDTO {
    @NotEmpty
    private String id;

    @NotEmpty
    private String name;

    private List<CreateCourseDTOResponseDTO> courses = new ArrayList<>();

    private List<CreateStudentResponseDTO> students = new ArrayList<>();

    private List<CreateEducatorResponseDTO> educators = new ArrayList<>();
    
    private List<SubjectResponseDTO> subjects = new ArrayList<>();
}
