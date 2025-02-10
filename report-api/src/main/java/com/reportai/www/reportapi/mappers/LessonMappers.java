package com.reportai.www.reportapi.mappers;

import com.reportai.www.reportapi.api.v1.lessons.requests.CreateLessonDTO;
import com.reportai.www.reportapi.api.v1.lessons.responses.CreateLessonResponse;
import com.reportai.www.reportapi.api.v1.lessons.responses.ExpandedLessonResponse;
import com.reportai.www.reportapi.entities.Lesson;
import java.util.UUID;

public class LessonMappers {
    private LessonMappers() {
    }

    public static Lesson convert(CreateLessonDTO createLessonDTO, UUID tenantId) {
        return Lesson.builder()
                .date(createLessonDTO.getDate())
                .name(createLessonDTO.getName())
                .status(Lesson.STATUS.UPCOMING)
                .startTime(createLessonDTO.getStartTime())
                .endTime(createLessonDTO.getEndTime())
                .description(createLessonDTO.getDescription())
                .day(createLessonDTO.getDate().getDayOfWeek())
                .tenantId(tenantId.toString())
                .build();
    }

    public static CreateLessonResponse convert(Lesson lesson) {
        return CreateLessonResponse
                .builder()
                .id(lesson.getId().toString())
                .name(lesson.getName())
                .date(lesson.getDate())
                .day(lesson.getDay())
                .startTime(lesson.getStartTime())
                .endTime(lesson.getEndTime())
                .status(lesson.getStatus())
                .build();
    }

    public static ExpandedLessonResponse convertToExpanded(Lesson lesson) {
        return ExpandedLessonResponse
                .builder()
                .id(lesson.getId().toString())
                .name(lesson.getName())
                .date(lesson.getDate())
                .day(lesson.getDay())
                .startTime(lesson.getStartTime())
                .endTime(lesson.getEndTime())
                .status(lesson.getStatus())
                .educators(lesson.getCourse().getEducators().stream().map(EducatorMappers::convert).toList())
                .students(lesson.getStudents().stream().map(StudentMappers::convert).toList())
                .course(CourseMappers.convert(lesson.getCourse()))
                .build();
    }
}
