package com.reportai.www.reportapi.api.v1.courses.responses;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.reportai.www.reportapi.entities.PriceRecord;
import com.reportai.www.reportapi.entities.courses.Course;
import jakarta.validation.constraints.NotEmpty;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CourseResponseDTO {

    @NotEmpty
    private String id;

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

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant courseStartTimestamptz;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant courseEndTimestamptz;

}
