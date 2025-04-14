package com.reportai.www.reportapi.mappers;

import com.reportai.www.reportapi.api.v1.lessons.requests.CreateLessonRequestDTO;
import com.reportai.www.reportapi.api.v1.lessons.responses.ExpandedLessonResponseDTO;
import com.reportai.www.reportapi.api.v1.lessons.responses.LessonResponseDTO;
import com.reportai.www.reportapi.entities.lessons.Lesson;
import com.reportai.www.reportapi.entities.views.LessonView;
import java.util.UUID;

public class LessonMappers {
    private LessonMappers() {
    }

    public static Lesson convert(CreateLessonRequestDTO createLessonRequestDTO, UUID tenantId) {

        return Lesson.builder()
                .lessonStartTimestamptz(createLessonRequestDTO.getLessonStartTimestamptz())
                .lessonEndTimestamptz(createLessonRequestDTO.getLessonEndTimestamptz())
                .name(createLessonRequestDTO.getName())
                .description(createLessonRequestDTO.getDescription())
                .tenantId(tenantId.toString())
                .build();
    }

    public static LessonResponseDTO convert(LessonView lesson) {
        return LessonResponseDTO
                .builder()
                .id(lesson.getId().toString())
                .name(lesson.getName())
                .description(lesson.getDescription())
                .students(lesson.getStudentLessonRegistrations().stream().map(lessonRegistration -> StudentMappers.convert(lessonRegistration.getStudent())).toList())
                .educators(lesson
                        .getEducatorLessonAttachments()
                        .stream()
                        .map(
                                educatorLessonAttachment -> EducatorMappers.convert(educatorLessonAttachment.getEducator())
                        ).toList())
                .lessonStatus(lesson.getLessonStatus())
                .lessonReviewStatus(lesson.getLessonReviewStatus())
                .lessonPlanStatus(lesson.getLessonPlanStatus())
                .lessonStartTimestamptz(lesson.getLessonStartTimestamptz())
                .lessonEndTimestamptz(lesson.getLessonEndTimestamptz())
                .build();
    }


    public static ExpandedLessonResponseDTO convertToExpanded(LessonView lessonView) {
        return ExpandedLessonResponseDTO
                .builder()
                .id(lessonView.getId().toString())
                .name(lessonView.getName())
                .lessonStatus(lessonView.getLessonStatus())
                .lessonPlanStatus(lessonView.getLessonPlanStatus())
                .lessonReviewStatus(lessonView.getLessonReviewStatus())
                .lessonStartTimestamptz(lessonView.getLessonStartTimestamptz())
                .lessonEndTimestamptz(lessonView.getLessonEndTimestamptz())
                .educators(lessonView
                        .getEducatorLessonAttachments()
                        .stream()
                        .map(
                                educatorLessonAttachment -> EducatorMappers.convert(educatorLessonAttachment.getEducator())
                        ).toList())
                .students(lessonView
                        .getStudentLessonRegistrations()
                        .stream()
                        .map(lessonViewRegistration -> StudentMappers.convert(lessonViewRegistration.getStudent())).toList())
                .course(CourseMappers.convert(lessonView.getCourse()))
                .subject(lessonView.getSubject() != null ? SubjectMappers.convert(lessonView.getSubject()) : null)
                .build();
    }
}
