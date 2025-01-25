package com.reportai.www.reportapi.api.v1.courses.requests;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.reportai.www.reportapi.entities.Course;
import com.reportai.www.reportapi.entities.PriceRecord;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateCourseDTO {

    @NotEmpty
    private Course.LESSON_FREQUENCY lessonFrequency;

    @NotEmpty
    private Double price;

    @NotEmpty
    private PriceRecord.FREQUENCY priceFrequency;

    @NotEmpty
    private String name;

    @NotEmpty
    private Integer maxSize;

    @DateTimeFormat
    private LocalDate startDate;

    @DateTimeFormat
    private LocalDate endDate;

    @DateTimeFormat
    private LocalTime startTime;

    @DateTimeFormat
    private LocalTime endTime;

    @NotEmpty
    private List<UUID> subjectIds;

    @NotEmpty
    private UUID levelId;

    @NotEmpty
    private List<UUID> educatorIds;
}
