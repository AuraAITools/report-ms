package com.reportai.www.reportapi.api.v1.students.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
public class CreateStudentDTO {

    @NotEmpty
    private String name;

    @Email
    @NotEmpty
    private String email;

    @DateTimeFormat
    @NotNull
    private LocalDateTime dateOfBirth;

    @NotEmpty
    private String currentSchool;

    @NotNull
    private UUID levelId;

}
