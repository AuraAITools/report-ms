package com.reportai.www.reportapi.api.v1.lessons.responses;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.reportai.www.reportapi.api.v1.accounts.responses.EducatorResponseDTO;
import com.reportai.www.reportapi.api.v1.accounts.responses.StudentResponseDTO;
import com.reportai.www.reportapi.entities.views.LessonView;
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
public class LessonResponseDTO {

    @NotEmpty
    private String id;

    @NotEmpty
    private String name;

    @NotNull
    private LessonView.LESSON_STATUS lessonStatus;

    @NotNull
    private LessonView.LESSON_REVIEW_STATUS lessonReviewStatus;

    @NotNull
    private LessonView.LESSON_PLAN_STATUS lessonPlanStatus;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant lessonStartTimestamptz;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant lessonEndTimestamptz;

    @NotEmpty
    private String description;


    private List<StudentResponseDTO> students = new ArrayList<>();

    private List<EducatorResponseDTO> educators = new ArrayList<>();
}
