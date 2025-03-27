package com.reportai.www.reportapi.api.v1.courses.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.reportai.www.reportapi.entities.PriceRecord;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class CreateCourseRequestDTO {

    @NotNull
    private Integer lessonNumberFrequency;

    @NotNull
    private Integer lessonWeeklyFrequency;

    @NotNull
    private Double price;

    @NotNull
    private PriceRecord.FREQUENCY priceFrequency;

    @NotEmpty
    private String name;

    @NotNull
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

    @NotNull
    private UUID levelId;

    private List<UUID> educatorIds = new ArrayList<>();

}
