package com.reportai.www.reportapi.api.v1.students.responses;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.reportai.www.reportapi.api.v1.accounts.responses.StudentClientAccountResponseDTO;
import com.reportai.www.reportapi.api.v1.courses.responses.CourseResponseDTO;
import com.reportai.www.reportapi.api.v1.levels.responses.LevelsResponseDTO;
import com.reportai.www.reportapi.api.v1.outlets.responses.OutletResponseDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@Builder
public class ExpandedStudentResponseDTO {

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
    private String school;

    @NotNull
    private LevelsResponseDTO level;

    @NotNull
    @Builder.Default
    private List<StudentClientAccountResponseDTO> accounts = new ArrayList<>();

    @NotNull
    @Builder.Default
    private List<OutletResponseDTO> outlets = new ArrayList<>();

    @NotNull
    @Builder.Default
    private List<CourseResponseDTO> courses = new ArrayList<>();

}
