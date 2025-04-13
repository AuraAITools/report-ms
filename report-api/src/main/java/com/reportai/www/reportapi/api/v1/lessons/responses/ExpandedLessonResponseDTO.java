package com.reportai.www.reportapi.api.v1.lessons.responses;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.reportai.www.reportapi.api.v1.accounts.responses.EducatorResponseDTO;
import com.reportai.www.reportapi.api.v1.accounts.responses.StudentResponseDTO;
import com.reportai.www.reportapi.api.v1.courses.responses.CourseResponseDTO;
import com.reportai.www.reportapi.api.v1.subjects.responses.SubjectResponseDTO;
import com.reportai.www.reportapi.entities.lessons.LessonView;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ExpandedLessonResponseDTO {

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

    @DateTimeFormat
    private LocalDate date;

    @DateTimeFormat
    private LocalTime startTime;

    @DateTimeFormat
    private LocalTime endTime;

    @NotEmpty
    private String description;

    @NotEmpty
    private DayOfWeek day;

    @NotNull
    private List<EducatorResponseDTO> educators;

    @NotNull
    private List<StudentResponseDTO> students;

    @NotNull
    private CourseResponseDTO course;

    @NotNull
    private SubjectResponseDTO subject;
}
