package com.reportai.www.reportapi.api.v1.accounts.responses;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.reportai.www.reportapi.api.v1.courses.responses.CourseResponseDTO;
import com.reportai.www.reportapi.api.v1.levels.responses.LevelsResponseDTO;
import com.reportai.www.reportapi.api.v1.outlets.responses.OutletResponseDTO;
import com.reportai.www.reportapi.api.v1.subjects.responses.SubjectResponseDTO;
import com.reportai.www.reportapi.entities.educators.Educator;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@Builder
public class ExpandedEducatorResponseDTO {

    @NotEmpty
    private String id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String email;

    @DateTimeFormat
    @NotNull
    private LocalDate dateOfBirth;

    @NotEmpty
    private String startDate;

    @NotNull
    private Educator.EMPLOYMENT_TYPE employmentType;

    @Builder.Default
    private List<LevelsResponseDTO> levels = new ArrayList<>();

    @Builder.Default
    private List<SubjectResponseDTO> subjects = new ArrayList<>();

    @Builder.Default
    private List<OutletResponseDTO> outlets = new ArrayList<>();

    @Builder.Default
    private List<CourseResponseDTO> courses = new ArrayList<>();

    @NotNull
    @Builder.Default
    private List<AccountResponseDTO> accounts = new ArrayList<>();


}
