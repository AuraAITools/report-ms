package com.reportai.www.reportapi.api.v1.courses.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.reportai.www.reportapi.api.v1.lessons.requests.CreateLessonRequestDTO;
import com.reportai.www.reportapi.entities.PriceRecord;
import com.reportai.www.reportapi.entities.courses.Course;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateCourseRequestDTO {


    @NotNull
    private Course.LESSON_FREQUENCY lessonFrequency;

    @NotNull
    private Double price;

    @NotNull
    private PriceRecord.FREQUENCY priceFrequency;

    @NotEmpty
    private String name;

    @NotNull
    private Integer maxSize;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) // ISO 8601 format
    @FutureOrPresent
    private Instant courseStartTimestamptz;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) // ISO 8601 format
    @FutureOrPresent
    private Instant courseEndTimestamptz;

    @NotEmpty
    private List<UUID> subjectIds;

    @NotNull
    private UUID levelId;

    private UUID outletRoomId;

    private List<UUID> studentIds = new ArrayList<>();

    private List<UUID> educatorIds = new ArrayList<>();

    private List<CreateLessonRequestDTO> lessons = new ArrayList<>();

}
