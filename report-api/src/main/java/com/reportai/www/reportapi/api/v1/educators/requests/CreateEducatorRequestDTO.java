package com.reportai.www.reportapi.api.v1.educators.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.reportai.www.reportapi.entities.educators.Educator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateEducatorRequestDTO {

    @NotEmpty
    private String name;

    @Email
    @NotEmpty
    private String email;

    @DateTimeFormat
    @NotNull
    private LocalDate dateOfBirth;

    @DateTimeFormat
    private LocalDate startDate;

    @NotNull
    private Educator.EMPLOYMENT_TYPE employmentType;

    @NotNull
    private List<UUID> levelIds = new ArrayList<>();

    @NotNull
    private List<UUID> subjectIds = new ArrayList<>();

    private List<UUID> outletIds = new ArrayList<>();

    private List<UUID> courseIds = new ArrayList<>();


}
