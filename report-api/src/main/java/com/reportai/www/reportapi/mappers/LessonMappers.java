package com.reportai.www.reportapi.mappers;

import com.reportai.www.reportapi.api.v1.lessons.requests.CreateLessonRequestDTO;
import com.reportai.www.reportapi.api.v1.lessons.responses.ExpandedLessonResponseDTO;
import com.reportai.www.reportapi.api.v1.lessons.responses.LessonResponseDTO;
import com.reportai.www.reportapi.entities.lessons.Lesson;
import com.reportai.www.reportapi.entities.lessons.LessonView;
import java.util.UUID;

public class LessonMappers {
    private LessonMappers() {
    }

    public static Lesson convert(CreateLessonRequestDTO createLessonRequestDTO, UUID tenantId) {

        return Lesson.builder()
                .date(createLessonRequestDTO.getDate())
                .name(createLessonRequestDTO.getName())
//                .status(Lesson.STATUS.UPCOMING)
                .startTime(createLessonRequestDTO.getStartTime())
                .endTime(createLessonRequestDTO.getEndTime())
                .day(createLessonRequestDTO.getDate().getDayOfWeek())
                .description(createLessonRequestDTO.getDescription())
                .tenantId(tenantId.toString())
                .build();
    }

    public static LessonResponseDTO convert(Lesson lesson) {
        return LessonResponseDTO
                .builder()
                .id(lesson.getId().toString())
                .name(lesson.getName())
                .date(lesson.getDate())
                .day(lesson.getDay())
                .description(lesson.getDescription())
                .students(lesson.getStudentLessonRegistrations().stream().map(lessonRegistration -> StudentMappers.convert(lessonRegistration.getStudent())).toList())
                .educators(lesson
                        .getEducatorLessonAttachments()
                        .stream()
                        .map(
                                educatorLessonAttachment -> EducatorMappers.convert(educatorLessonAttachment.getEducator())
                        ).toList())
                .startTime(lesson.getStartTime())
                .endTime(lesson.getEndTime())
//                .status(lesson.getStatus())
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
//                .status(lesson.getStatus())
                .educators(lesson
                        .getEducatorLessonAttachments()
                        .stream()
                        .map(
                                educatorLessonAttachment -> EducatorMappers.convert(educatorLessonAttachment.getEducator())
                        ).toList())
                .students(lesson
                        .getStudentLessonRegistrations()
                        .stream()
                        .map(lessonRegistration -> StudentMappers.convert(lessonRegistration.getStudent())).toList())
                .course(CourseMappers.convert(lesson.getCourse()))
                .subject(lesson.getSubject() != null ? SubjectMappers.convert(lesson.getSubject()) : null)
                .build();
    }

    public static ExpandedLessonResponseDTO convertToExpanded(LessonView lessonView) {

        return ExpandedLessonResponseDTO
                .builder()
                .id(lessonView.getId().toString())
                .name(lessonView.getName())
                .date(lessonView.getDate())
                .day(lessonView.getDay())
                .startTime(lessonView.getStartTime())
                .endTime(lessonView.getEndTime())
                .lessonStatus(lessonView.getLessonStatus())
                .lessonPlanStatus(lessonView.getLessonPlanStatus())
                .lessonReviewStatus(lessonView.getLessonReviewStatus())
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
