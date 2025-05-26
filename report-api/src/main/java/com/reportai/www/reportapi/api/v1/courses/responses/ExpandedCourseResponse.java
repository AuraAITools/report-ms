package com.reportai.www.reportapi.api.v1.courses.responses;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.reportai.www.reportapi.api.v1.lessons.responses.LessonResponseDTO;
import com.reportai.www.reportapi.api.v1.levels.responses.LevelsResponseDTO;
import com.reportai.www.reportapi.api.v1.outlets.responses.OutletResponseDTO;
import com.reportai.www.reportapi.api.v1.subjects.responses.SubjectResponseDTO;
import com.reportai.www.reportapi.entities.PriceRecord;
import com.reportai.www.reportapi.entities.courses.Course;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ExpandedCourseResponse {

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

    private OutletResponseDTO outlet;

    private LevelsResponseDTO level;

    @NotNull
    @Builder.Default
    private List<LessonResponseDTO> lessons = new ArrayList<>();

    @NotNull
    @Builder.Default
    private List<SubjectResponseDTO> subjects = new ArrayList<>();

}
