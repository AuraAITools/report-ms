package com.reportai.www.reportapi.mappers;

import com.reportai.www.reportapi.api.v1.lessons.requests.CreateLessonRequestDTO;
import com.reportai.www.reportapi.api.v1.lessons.responses.CreateLessonResponseDTO;
import com.reportai.www.reportapi.api.v1.lessons.responses.ExpandedLessonResponseDTO;
import com.reportai.www.reportapi.entities.Lesson;
import java.util.UUID;

public class LessonMappers {
    private LessonMappers() {
    }

    public static Lesson convert(CreateLessonRequestDTO createLessonRequestDTO, UUID tenantId) {

        return Lesson.builder()
                .date(createLessonRequestDTO.getDate())
                .name(createLessonRequestDTO.getName())
                .status(Lesson.STATUS.UPCOMING)
                .startTime(createLessonRequestDTO.getStartTime())
                .endTime(createLessonRequestDTO.getEndTime())
                .day(createLessonRequestDTO.getDate().getDayOfWeek())
                .tenantId(tenantId.toString())
                .build();
    }

    public static CreateLessonResponseDTO convert(Lesson lesson) {
        return CreateLessonResponseDTO
                .builder()
                .id(lesson.getId().toString())
                .name(lesson.getName())
                .date(lesson.getDate())
                .day(lesson.getDay())
                .students(lesson.getStudents().stream().map(StudentMappers::convert).toList())
                .educators(lesson.getEducators().stream().map(EducatorMappers::convert).toList())
                .startTime(lesson.getStartTime())
                .endTime(lesson.getEndTime())
                .status(lesson.getStatus())
                .build();
    }

    public static ExpandedLessonResponseDTO convertToExpanded(Lesson lesson) {
        return ExpandedLessonResponseDTO
                .builder()
                .id(lesson.getId().toString())
                .name(lesson.getName())
                .date(lesson.getDate())
                .day(lesson.getDay())
                .startTime(lesson.getStartTime())
                .endTime(lesson.getEndTime())
                .status(lesson.getStatus())
                .educators(lesson.getEducators().stream().map(EducatorMappers::convert).toList())
                .students(lesson.getStudents().stream().map(StudentMappers::convert).toList())
                .course(CourseMappers.convert(lesson.getCourse()))
                .build();
    }
}
