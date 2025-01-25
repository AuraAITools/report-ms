package com.reportai.www.reportapi.api.v1.students.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
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
    private LocalDateTime dateOfBirth;

    @NotEmpty
    private String currentSchool;

    @NotEmpty
    private String currentLevel;
}
