package com.reportai.www.reportapi.api.v1.accounts.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.reportai.www.reportapi.entities.educators.Educator;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
public class CreateEducatorRequestDTO {
    @NotEmpty
    private String name;

    @NotEmpty
    private String email;

    @NotNull
    private List<UUID> levelIds = new ArrayList<>();

    @NotNull
    private List<UUID> subjectIds = new ArrayList<>();

    @DateTimeFormat
    private LocalDate startDate;

    @NotNull
    private Educator.EMPLOYMENT_TYPE employmentType;

    @NotNull
    private LocalDate dateOfBirth;
}
