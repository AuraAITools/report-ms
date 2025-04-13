package com.reportai.www.reportapi.api.v1.courses.responses;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.reportai.www.reportapi.entities.PriceRecord;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CourseResponseDTO {

    @NotEmpty
    private String id;

    @NotEmpty
    private Integer lessonNumberFrequency;

    @NotEmpty
    private Integer lessonWeeklyFrequency;

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
    private List<LessonGenerationTemplateResponseDTO> lessonGenerationTemplates;
}
