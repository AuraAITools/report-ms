package com.reportai.www.reportapi.api.v1.accounts.responses;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.reportai.www.reportapi.api.v1.courses.responses.CourseResponseDTO;
import com.reportai.www.reportapi.api.v1.levels.responses.LevelsResponseDTO;
import com.reportai.www.reportapi.api.v1.outlets.responses.OutletResponseDTO;
import com.reportai.www.reportapi.entities.personas.StudentClientPersona;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@Builder
public class StudentResponseDTO {

    @NotEmpty
    private String id;

    @NotEmpty
    private String name;

    @Email
    @NotEmpty
    private String email;

    @DateTimeFormat
    @NotEmpty
    private LocalDate dateOfBirth;

    @NotEmpty
    private String currentSchool;

    @NotEmpty
    private String currentLevel;

    @NotEmpty
    private StudentClientPersona.RELATIONSHIP relationship;

    @NotNull
    private LevelsResponseDTO level;

    @NotNull
    private List<OutletResponseDTO> outlets;

    @NotNull
    private List<CourseResponseDTO> courses;

    @NotEmpty
    private String contact;
}
