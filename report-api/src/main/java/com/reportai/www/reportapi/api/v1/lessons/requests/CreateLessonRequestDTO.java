package com.reportai.www.reportapi.api.v1.lessons.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;


import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@JsonInclude(NON_EMPTY)
public class CreateLessonRequestDTO {

    @NotEmpty
    private String name;

    private String description;

    @DateTimeFormat
    private LocalDate date;

    @DateTimeFormat
    private LocalTime startTime;

    @DateTimeFormat
    private LocalTime endTime;

    @NotNull
    private List<UUID> educatorIds = new ArrayList<>();

    @NotNull
    private List<UUID> studentIds = new ArrayList<>();

}
