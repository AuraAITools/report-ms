package com.reportai.www.reportapi.api.v1.accounts.responses;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.reportai.www.reportapi.api.v1.courses.responses.CreateCourseDTOResponseDTO;
import com.reportai.www.reportapi.api.v1.levels.responses.CreateLevelsResponseDTO;
import com.reportai.www.reportapi.api.v1.outlets.responses.CreateOutletResponseDto;
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
public class CreateStudentResponseDTO {

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
    private CreateLevelsResponseDTO level;

    @NotNull
    private List<CreateOutletResponseDto> outlets;

    @NotNull
    private List<CreateCourseDTOResponseDTO> courses;

    @NotEmpty
    private String contact;
}
