package com.reportai.www.reportapi.api.v1.lessons.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import java.time.Instant;
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

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) // ISO 8601 format
    @FutureOrPresent
    private Instant lessonStartTimestamptz;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) // ISO 8601 format
    @FutureOrPresent
    private Instant lessonEndTimestamptz;

    private List<UUID> educatorIds = new ArrayList<>();

    private List<UUID> studentIds = new ArrayList<>();

    private UUID outletRoomId;

    private UUID subjectId;

}
