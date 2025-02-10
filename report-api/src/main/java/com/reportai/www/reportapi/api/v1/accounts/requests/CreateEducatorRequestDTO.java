package com.reportai.www.reportapi.api.v1.accounts.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.reportai.www.reportapi.entities.Educator;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
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

    @NotEmpty
    private List<UUID> levelIds;

    @NotEmpty
    private List<UUID> subjectIds;

    @DateTimeFormat
    private LocalDate startDate;

    @NotNull
    private Educator.EMPLOYMENT_TYPE employmentType;
}
