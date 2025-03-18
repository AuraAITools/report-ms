package com.reportai.www.reportapi.api.v1.courses.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.reportai.www.reportapi.entities.PriceRecord;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateCourseRequestDTO {

    private Integer lessonNumberFrequency;

    private Integer lessonWeeklyFrequency;

    private Double price;

    private PriceRecord.FREQUENCY priceFrequency;

    private String name;

    private Integer maxSize;

    @DateTimeFormat
    private LocalDate startDate;

    @DateTimeFormat
    private LocalDate endDate;

    @DateTimeFormat
    private LocalTime startTime;

    @DateTimeFormat
    private LocalTime endTime;

    /**
     * TODO: figure out how to patch subjects
     */
    private List<UUID> subjectIds = new ArrayList<>();

    private UUID levelId;

    private List<UUID> educatorIds = new ArrayList<>();
}
