package com.reportai.www.reportapi.api.v1.lessons.responses;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.reportai.www.reportapi.api.v1.accounts.responses.EducatorResponseDTO;
import com.reportai.www.reportapi.api.v1.accounts.responses.StudentResponseDTO;
import com.reportai.www.reportapi.entities.lessons.Lesson;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
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
    private Lesson.LESSON_STATUS status;

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

    private List<StudentResponseDTO> students = new ArrayList<>();

    private List<EducatorResponseDTO> educators = new ArrayList<>();
}
