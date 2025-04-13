package com.reportai.www.reportapi.api.v1.lessons.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.reportai.www.reportapi.entities.lessons.Lesson;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;


import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@JsonInclude(NON_EMPTY)
public class UpdateLessonRequestDTO {

    private String name;

    private String description;

    private Lesson.LESSON_STATUS status;

    @DateTimeFormat
    private LocalDate date;

    @DateTimeFormat
    private LocalTime startTime;

    @DateTimeFormat
    private LocalTime endTime;

    private UUID subjectId;

    private List<UUID> educatorIds;

    private List<UUID> studentIds;

}
