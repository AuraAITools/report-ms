package com.aura.details.services;

import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.entities.lessons.Lesson;
import com.aura.core.entities.lessons.LessonPlan;
import com.aura.core.repositories.EducatorRepository;
import com.aura.core.repositories.LessonOutletRoomBookingRepository;
import com.aura.core.repositories.LessonRepository;
import com.aura.core.repositories.StudentRepository;
import com.aura.core.usecases.lessons.AddEducatorsToLessonUseCase;
import com.aura.core.usecases.lessons.AddLevelsToLessonUseCase;
import com.aura.core.usecases.lessons.AddStudentsToLessonUseCase;
import com.aura.core.usecases.lessons.AddSubjectsToLessonUseCase;
import com.aura.core.usecases.lessons.AddTopicsToLessonUseCase;
import com.aura.core.usecases.lessons.CreateLessonUseCase;
import com.aura.core.usecases.lessons.DeleteLessonUseCase;
import com.aura.core.usecases.lessons.LessonQueryUseCase;
import com.aura.core.usecases.lessons.LinkLessonsToCourseUseCase;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@DisplayName("Lesson service")
@ExtendWith(MockitoExtension.class)
class LessonServiceTest {

    @Mock
    private CreateLessonUseCase createLessonUseCase;
    @Mock
    private AddEducatorsToLessonUseCase addEducatorsToLessonUseCase;
    @Mock
    private AddStudentsToLessonUseCase addStudentsToLessonUseCase;
    @Mock
    private AddSubjectsToLessonUseCase addSubjectsToLessonUseCase;
    @Mock
    private AddLevelsToLessonUseCase addLevelsToLessonUseCase;
    @Mock
    private AddTopicsToLessonUseCase addTopicsToLessonUseCase;
    @Mock
    private LinkLessonsToCourseUseCase linkLessonsToCourseUseCase;
    @Mock
    private DeleteLessonUseCase deleteLessonUseCase;
    @Mock
    private LessonRepository lessonRepository;
    @Mock
    private LessonOutletRoomBookingRepository lessonOutletRoomBookingRepository;
    @Mock
    private RelationshipAuthorizationService relationshipService;
    @Mock
    private LessonQueryUseCase lessonQueryUseCase;
    @Mock
    private EducatorRepository educatorRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private LessonPlanService lessonPlanService;

    @InjectMocks
    private LessonService lessonService;

    @DisplayName("Should write all three structural relationships in a single batch")
    @Test
    void createLesson_writesStructuralRelationshipsAsBatch() {
        String courseId = "course-001";
        String tenantId = "tenant-789";
        Lesson input = Lesson.builder().name("Course Lesson").build();
        Lesson created = Lesson.builder().id("lesson-123").name("Course Lesson").tenantId(tenantId).build();
        when(createLessonUseCase.createLessonInCourse(any(), eq(courseId))).thenReturn(created);

        lessonService.createLesson(input, courseId, "room-1",
                List.of(), List.of(), List.of(), List.of(), List.of(), List.of());

        verify(relationshipService).writeRelationships(argThat(list ->
                list.containsAll(List.of(
                        new RelationshipAuthorizationService.Relationship(
                                "lesson", "lesson-123", "user_group", "lesson_user_group", "lesson-123"),
                        new RelationshipAuthorizationService.Relationship(
                                "lesson_user_group", "lesson-123", "parent", "system_user_group", "aura"),
                        new RelationshipAuthorizationService.Relationship(
                                "lesson", "lesson-123", "parent", "course", "course-001")))
                        && list.size() == 3));
        verifyNoMoreInteractions(relationshipService);
    }

    @DisplayName("Should call all association use cases when ID lists are non-empty")
    @Test
    void createLesson_callsAllAssociationUseCases() {
        String courseId = "course-001";
        String tenantId = "tenant-789";
        List<String> educatorIds = List.of("edu-1", "edu-2");
        List<String> studentIds = List.of("stu-1");
        List<String> subjectIds = List.of("sub-1", "sub-2");
        List<String> levelIds = List.of("lvl-1");
        List<String> topicIds = List.of("top-1", "top-2", "top-3");

        Lesson input = Lesson.builder().name("Full Lesson").tenantId(tenantId).build();
        Lesson created = Lesson.builder().id("lesson-123").name("Full Lesson").tenantId(tenantId).build();
        when(createLessonUseCase.createLessonInCourse(any(), eq(courseId))).thenReturn(created);
        when(addEducatorsToLessonUseCase.addEducatorsToLessonUseCase("lesson-123", educatorIds, tenantId))
                .thenReturn(List.of());
        when(addStudentsToLessonUseCase.addStudentsToLesson("lesson-123", studentIds, tenantId))
                .thenReturn(List.of());
        when(addSubjectsToLessonUseCase.addSubjectsToLesson("lesson-123", subjectIds, tenantId))
                .thenReturn(List.of());
        when(addLevelsToLessonUseCase.addLevelsToLesson("lesson-123", levelIds, tenantId))
                .thenReturn(List.of());
        when(addTopicsToLessonUseCase.addTopicsToLesson("lesson-123", topicIds, tenantId))
                .thenReturn(List.of());
        when(educatorRepository.findUserIdsByEducatorIds(educatorIds)).thenReturn(Map.of());
        when(studentRepository.findUserIdsByStudentIds(studentIds)).thenReturn(Map.of());

        lessonService.createLesson(input, courseId, "room-1",
                educatorIds, studentIds, subjectIds, levelIds, topicIds, List.of());

        verify(addEducatorsToLessonUseCase).addEducatorsToLessonUseCase("lesson-123", educatorIds, tenantId);
        verify(addStudentsToLessonUseCase).addStudentsToLesson("lesson-123", studentIds, tenantId);
        verify(addSubjectsToLessonUseCase).addSubjectsToLesson("lesson-123", subjectIds, tenantId);
        verify(addLevelsToLessonUseCase).addLevelsToLesson("lesson-123", levelIds, tenantId);
        verify(addTopicsToLessonUseCase).addTopicsToLesson("lesson-123", topicIds, tenantId);
        verifyNoInteractions(lessonPlanService);
    }

    @DisplayName("Should delegate each lesson plan to LessonPlanService.createLessonPlan after lesson creation")
    @Test
    void createLesson_withLessonPlans_delegatesToLessonPlanService() {
        String courseId = "course-001";
        String tenantId = "tenant-789";
        Lesson input = Lesson.builder().name("Lesson w/ Plans").build();
        Lesson created = Lesson.builder().id("lesson-123").name("Lesson w/ Plans").tenantId(tenantId).build();
        when(createLessonUseCase.createLessonInCourse(any(), eq(courseId))).thenReturn(created);

        LessonPlan plan1 = LessonPlan.builder().plan("Plan A").build();
        LessonPlan plan2 = LessonPlan.builder().plan("Plan B").build();
        LessonService.NewLessonPlan np1 = new LessonService.NewLessonPlan(
                plan1, List.of("top-1"), List.of("stu-1"), List.of("mat-1"), List.of());
        LessonService.NewLessonPlan np2 = new LessonService.NewLessonPlan(
                plan2, List.of(), List.of(), List.of(), List.of());

        lessonService.createLesson(input, courseId, "room-1",
                List.of(), List.of(), List.of(), List.of(), List.of(),
                List.of(np1, np2));

        verify(lessonPlanService).createLessonPlan(
                plan1, "lesson-123", List.of("top-1"), List.of("stu-1"), List.of("mat-1"), List.of());
        verify(lessonPlanService).createLessonPlan(
                plan2, "lesson-123", List.of(), List.of(), List.of(), List.of());
    }

    @DisplayName("Should delete lesson and wipe spicedb relationships in order")
    @Test
    void deleteById_clearsLinksAndSpicedb() {
        lessonService.deleteById("lesson-1");

        InOrder ordered = inOrder(lessonRepository, deleteLessonUseCase, relationshipService);
        ordered.verify(lessonRepository).deleteAllLessonLinksByLessonId("lesson-1");
        ordered.verify(deleteLessonUseCase).deleteById("lesson-1");
        ordered.verify(relationshipService).deleteAllRelationshipsForResource("lesson", "lesson-1");
        ordered.verify(relationshipService).deleteAllRelationshipsForResource("lesson_user_group", "lesson-1");
    }
}
