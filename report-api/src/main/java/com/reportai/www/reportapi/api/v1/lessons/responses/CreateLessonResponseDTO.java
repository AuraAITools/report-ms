package com.reportai.www.reportapi.api.v1.lessons.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.reportai.www.reportapi.entities.Lesson;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;


import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@Getter
@Setter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(NON_EMPTY)
public class CreateLessonResponseDTO {

    @NotEmpty
    private String id;

    @NotEmpty
    private String name;

    @NotNull
    private Lesson.STATUS status;

    @DateTimeFormat
    private LocalDate date;

    @DateTimeFormat
    private LocalTime startTime;

    @DateTimeFormat
    private LocalTime endTime;

    @NotEmpty
    private String description;

    @NotEmpty
    private DayOfWeek day;
}
