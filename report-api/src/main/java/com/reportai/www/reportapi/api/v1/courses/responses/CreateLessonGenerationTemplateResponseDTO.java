package com.reportai.www.reportapi.api.v1.courses.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
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
public class CreateLessonGenerationTemplateResponseDTO {

    @NotEmpty
    private String id;

    @NotEmpty
    private DayOfWeek dayOfWeek;

    @NotNull
    private Integer weekNumber;

    @DateTimeFormat
    private LocalTime startTime;

    @DateTimeFormat
    private LocalTime endTime;
}
