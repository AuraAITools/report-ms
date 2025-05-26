package com.reportai.www.reportapi.api.v1.students.responses;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDate;
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
    private String school;

}
