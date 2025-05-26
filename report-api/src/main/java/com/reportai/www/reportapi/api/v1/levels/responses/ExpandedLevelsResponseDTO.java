package com.reportai.www.reportapi.api.v1.levels.responses;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.reportai.www.reportapi.api.v1.accounts.responses.EducatorResponseDTO;
import com.reportai.www.reportapi.api.v1.courses.responses.CourseResponseDTO;
import com.reportai.www.reportapi.api.v1.students.responses.StudentResponseDTO;
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

    @Builder.Default
    private List<CourseResponseDTO> courses = new ArrayList<>();

    @Builder.Default
    private List<StudentResponseDTO> students = new ArrayList<>();

    @Builder.Default
    private List<EducatorResponseDTO> educators = new ArrayList<>();

    @Builder.Default
    private List<SubjectResponseDTO> subjects = new ArrayList<>();
}
