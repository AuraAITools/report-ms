package com.aura.details.services;

import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.entities.lessons.Lesson;
import com.aura.core.usecases.lessons.AddEducatorsToLessonUseCase;
import com.aura.core.usecases.lessons.CreateLessonUseCase;
import com.aura.core.usecases.lessons.LinkLessonsToCourseUseCase;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Lesson service")
@ExtendWith(MockitoExtension.class)
class LessonServiceTest {

    @Mock private CreateLessonUseCase createLessonUseCase;
    @Mock private AddEducatorsToLessonUseCase addEducatorsToLessonUseCase;
    @Mock private LinkLessonsToCourseUseCase linkLessonsToCourseUseCase;
    @Mock private RelationshipAuthorizationService relationshipService;

    @InjectMocks private LessonService lessonService;

    @DisplayName("Should create standalone lesson and write parent_outlet relationship")
    @Test
    void createStandaloneLesson_writesParentOutletRelationship() {
        String outletId = "outlet-456";
        Lesson input = Lesson.builder().name("Standalone").build();
        Lesson created = Lesson.builder().id("lesson-123").name("Standalone").tenantId("tenant-789").build();
        when(createLessonUseCase.createStandaloneLesson(any(), eq(outletId))).thenReturn(created);

        Lesson result = lessonService.createStandaloneLesson(input, outletId);

        assertThat(result).isEqualTo(created);
        verify(relationshipService).writeRelationship(
                "lesson", "lesson-123", "parent_outlet", "outlet", "outlet-456");
    }

    @DisplayName("Should write parent_course relationship when lesson has a course")
    @Test
    void createLesson_withCourse_writesParentCourseRelationship() {
        String outletId = "outlet-456";
        String courseId = "course-001";
        String tenantId = "tenant-789";
        Lesson input = Lesson.builder().name("Course Lesson").build();
        Lesson created = Lesson.builder().id("lesson-123").name("Course Lesson").tenantId(tenantId).build();
        when(createLessonUseCase.createStandaloneLesson(any(), eq(outletId))).thenReturn(created);

        lessonService.createLesson(input, outletId, tenantId, courseId, List.of());

        verify(relationshipService).writeRelationship(
                "lesson", "lesson-123", "parent_course", "course", "course-001");
    }

    @DisplayName("Should write parent_outlet relationship when lesson has no course")
    @Test
    void createLesson_withoutCourse_writesParentOutletRelationship() {
        String outletId = "outlet-456";
        String tenantId = "tenant-789";
        Lesson input = Lesson.builder().name("No Course").build();
        Lesson created = Lesson.builder().id("lesson-123").name("No Course").tenantId(tenantId).build();
        when(createLessonUseCase.createStandaloneLesson(any(), eq(outletId))).thenReturn(created);

        lessonService.createLesson(input, outletId, tenantId, null, List.of());

        verify(relationshipService).writeRelationship(
                "lesson", "lesson-123", "parent_outlet", "outlet", "outlet-456");
    }
}
