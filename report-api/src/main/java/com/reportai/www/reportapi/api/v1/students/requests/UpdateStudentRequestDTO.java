package com.reportai.www.reportapi.api.v1.students.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateStudentRequestDTO {
    private String name;

    @DateTimeFormat
    private LocalDate dateOfBirth;

    private String currentSchool;

    private UUID levelId;

    private List<UUID> outletIds = new ArrayList<>();

    private List<UUID> courseIds = new ArrayList<>();
}
