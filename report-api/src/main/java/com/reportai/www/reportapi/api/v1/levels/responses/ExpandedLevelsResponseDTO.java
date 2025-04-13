package com.reportai.www.reportapi.api.v1.levels.responses;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.reportai.www.reportapi.api.v1.accounts.responses.EducatorResponseDTO;
import com.reportai.www.reportapi.api.v1.accounts.responses.StudentResponseDTO;
import com.reportai.www.reportapi.api.v1.courses.responses.CourseResponseDTO;
import com.reportai.www.reportapi.api.v1.subjects.responses.SubjectResponseDTO;
import jakarta.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;
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
    private Set<CourseResponseDTO> courses = new HashSet<>();

    @Builder.Default
    private Set<StudentResponseDTO> students = new HashSet<>();

    @Builder.Default
    private Set<EducatorResponseDTO> educators = new HashSet<>();

    @Builder.Default
    private Set<SubjectResponseDTO> subjects = new HashSet<>();
}
