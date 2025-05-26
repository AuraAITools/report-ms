package com.reportai.www.reportapi.api.v1.accounts.responses;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.reportai.www.reportapi.entities.educators.Educator;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EducatorResponseDTO {

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


}
