package com.aura.details.services;

import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.entities.lessons.Lesson;
import com.aura.core.entities.lessons.LessonGroup;
import com.aura.core.entities.lessons.LessonGroupPlan;
import com.aura.core.exceptions.LessonConflictException;
import com.aura.core.repositories.EducatorRepository;
import com.aura.core.repositories.LessonOutletRoomBookingRepository;
import com.aura.core.repositories.LessonRepository;
import com.aura.core.repositories.StudentRepository;
import com.aura.core.usecases.lessons.ChangeLessonOutletRoomUseCase;
import com.aura.core.usecases.lessons.AddEducatorsToLessonUseCase;
import com.aura.core.usecases.lessons.AddLevelsToLessonUseCase;
import com.aura.core.usecases.lessons.AddStudentsToLessonUseCase;
import com.aura.core.usecases.lessons.AddSubjectsToLessonUseCase;
import com.aura.core.usecases.lessons.AddTopicsToLessonUseCase;
import com.aura.core.usecases.lessons.CreateLessonUseCase;
import com.aura.core.usecases.lessons.DeleteLessonUseCase;
import com.aura.core.usecases.lessons.DetectLessonConflictsUseCase;
import com.aura.core.usecases.lessons.LessonQueryUseCase;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
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
    private DetectLessonConflictsUseCase detectLessonConflictsUseCase;
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
    private LessonGroupService lessonGroupService;
    @Mock
    private LessonGroupPlanService lessonGroupPlanService;
    @Mock
    private StudentLessonReportService studentLessonReportService;
    @Mock
    private ChangeLessonOutletRoomUseCase changeLessonOutletRoomUseCase;

    @InjectMocks
    private LessonService lessonService;

    @BeforeEach
    void stubNoConflictsByDefault() {
        lenient().when(detectLessonConflictsUseCase.detectRoomConflicts(any(), any(), any(), any(), any()))
                .thenReturn(List.of());
        lenient().when(detectLessonConflictsUseCase.detectStudentConflicts(any(), any(), any(), any(), any()))
                .thenReturn(List.of());
        lenient().when(detectLessonConflictsUseCase.detectEducatorConflicts(any(), any(), any(), any(), any()))
                .thenReturn(List.of());
    }

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
                                "lesson_user_group", "lesson-123", "parent", "course_user_group", "course-001"),
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
        verifyNoInteractions(lessonGroupService, lessonGroupPlanService, studentLessonReportService);
    }

    @DisplayName("Should delegate each lesson group to LessonGroupService.createLessonGroup after lesson creation")
    @Test
    void createLesson_withLessonGroups_delegatesToLessonGroupService() {
        String courseId = "course-001";
        String tenantId = "tenant-789";
        Lesson input = Lesson.builder().name("Lesson w/ Groups").build();
        Lesson created = Lesson.builder().id("lesson-123").name("Lesson w/ Groups").tenantId(tenantId).build();
        when(createLessonUseCase.createLessonInCourse(any(), eq(courseId))).thenReturn(created);

        LessonGroupPlan plan1 = LessonGroupPlan.builder().plan("Plan A").build();
        LessonGroupPlan plan2 = LessonGroupPlan.builder().plan("Plan B").build();

        LessonGroup createdGroup1 = LessonGroup.builder().id("group-1").build();
        LessonGroup createdGroup2 = LessonGroup.builder().id("group-2").build();

        LessonService.NewLessonGroup ng1 = new LessonService.NewLessonGroup(
                "Group A", plan1, List.of("top-1"), List.of("stu-1"), List.of("mat-1"), List.of());
        LessonService.NewLessonGroup ng2 = new LessonService.NewLessonGroup(
                "Group B", plan2, List.of(), List.of(), List.of(), List.of());

        when(lessonGroupService.createLessonGroup(any(), eq("lesson-123")))
                .thenReturn(createdGroup1).thenReturn(createdGroup2);

        lessonService.createLesson(input, courseId, "room-1",
                List.of(), List.of(), List.of(), List.of(), List.of(),
                List.of(ng1, ng2));

        verify(lessonGroupService).createLessonGroup(
                argThat(g -> "Group A".equals(g.getName())), eq("lesson-123"));
        verify(lessonGroupService).createLessonGroup(
                argThat(g -> "Group B".equals(g.getName())), eq("lesson-123"));
        verify(lessonGroupPlanService).createLessonGroupPlan(
                plan1, "group-1", List.of("top-1"), List.of("mat-1"), List.of());
        verify(lessonGroupPlanService).createLessonGroupPlan(
                plan2, "group-2", List.of(), List.of(), List.of());
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

    @DisplayName("Should delegate findAllByEducatorId to the lesson query use case")
    @Test
    void findAllByEducatorId_delegatesToQueryUseCase() {
        Lesson l1 = Lesson.builder().id("l-1").build();
        when(lessonQueryUseCase.findAllByEducatorId("e-1")).thenReturn(List.of(l1));

        org.assertj.core.api.Assertions.assertThat(lessonService.findAllByEducatorId("e-1")).containsExactly(l1);
    }

    @DisplayName("Should delegate findAllByStudentId to the lesson query use case")
    @Test
    void findAllByStudentId_delegatesToQueryUseCase() {
        Lesson l1 = Lesson.builder().id("l-1").build();
        when(lessonQueryUseCase.findAllByStudentId("s-1")).thenReturn(List.of(l1));

        org.assertj.core.api.Assertions.assertThat(lessonService.findAllByStudentId("s-1")).containsExactly(l1);
    }

    @Nested
    @DisplayName("createLesson conflict checks")
    class CreateLessonConflicts {

        @Test
        @DisplayName("Should throw LessonConflictException(ROOM) and not persist when room is double-booked")
        void rejectsRoomConflict() {
            Lesson lesson = lessonStub("lesson-123", "tenant-1",
                    Instant.parse("2026-06-10T09:00:00Z"),
                    Instant.parse("2026-06-10T10:00:00Z"));
            when(createLessonUseCase.createLessonInCourse(any(), eq("course-1")))
                    .thenReturn(lesson);
            when(detectLessonConflictsUseCase.detectRoomConflicts(
                    "tenant-1", "room-1",
                    lesson.getLessonStartTimestamptz(), lesson.getLessonEndTimestamptz(), null))
                    .thenReturn(List.of("conflict-lesson"));

            assertThatThrownBy(() -> lessonService.createLesson(
                    lesson, "course-1", "room-1",
                    List.of(), List.of(), List.of(), List.of(), List.of(), List.of()))
                    .isInstanceOf(LessonConflictException.class)
                    .extracting("type").isEqualTo(LessonConflictException.ConflictType.ROOM);

            verify(lessonOutletRoomBookingRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw LessonConflictException(STUDENT) when an added student has a conflict")
        void rejectsStudentConflict() {
            Lesson lesson = lessonStub("lesson-123", "tenant-1",
                    Instant.parse("2026-06-10T09:00:00Z"),
                    Instant.parse("2026-06-10T10:00:00Z"));
            when(createLessonUseCase.createLessonInCourse(any(), eq("course-1")))
                    .thenReturn(lesson);
            when(detectLessonConflictsUseCase.detectStudentConflicts(
                    "tenant-1", "student-1",
                    lesson.getLessonStartTimestamptz(), lesson.getLessonEndTimestamptz(), null))
                    .thenReturn(List.of("conflict-lesson"));

            assertThatThrownBy(() -> lessonService.createLesson(
                    lesson, "course-1", null,
                    List.of(), List.of("student-1"), List.of(), List.of(), List.of(), List.of()))
                    .isInstanceOf(LessonConflictException.class)
                    .extracting("type").isEqualTo(LessonConflictException.ConflictType.STUDENT);

            verify(addStudentsToLessonUseCase, never()).addStudentsToLesson(any(), any(), any());
        }

        @Test
        @DisplayName("Should throw LessonConflictException(EDUCATOR) when an added educator has a conflict")
        void rejectsEducatorConflict() {
            Lesson lesson = lessonStub("lesson-123", "tenant-1",
                    Instant.parse("2026-06-10T09:00:00Z"),
                    Instant.parse("2026-06-10T10:00:00Z"));
            when(createLessonUseCase.createLessonInCourse(any(), eq("course-1")))
                    .thenReturn(lesson);
            when(detectLessonConflictsUseCase.detectEducatorConflicts(
                    "tenant-1", "educator-1",
                    lesson.getLessonStartTimestamptz(), lesson.getLessonEndTimestamptz(), null))
                    .thenReturn(List.of("conflict-lesson"));

            assertThatThrownBy(() -> lessonService.createLesson(
                    lesson, "course-1", null,
                    List.of("educator-1"), List.of(), List.of(), List.of(), List.of(), List.of()))
                    .isInstanceOf(LessonConflictException.class)
                    .extracting("type").isEqualTo(LessonConflictException.ConflictType.EDUCATOR);

            verify(addEducatorsToLessonUseCase, never()).addEducatorsToLessonUseCase(any(), any(), any());
        }

        private Lesson lessonStub(String id, String tenantId, Instant start, Instant end) {
            return Lesson.builder()
                    .id(id)
                    .tenantId(tenantId)
                    .lessonStartTimestamptz(start)
                    .lessonEndTimestamptz(end)
                    .build();
        }
    }

    @Nested
    @DisplayName("changeLessonOutletRoom")
    class ChangeLessonOutletRoom {

        @Test
        @DisplayName("Should delegate to use case and return result")
        void delegates() {
            Lesson lesson = Lesson.builder().id("lesson-1").build();
            when(changeLessonOutletRoomUseCase.changeLessonOutletRoom("lesson-1", "room-1"))
                    .thenReturn(lesson);

            Lesson result = lessonService.changeLessonOutletRoom("lesson-1", "room-1");

            assertThat(result).isSameAs(lesson);
            verify(changeLessonOutletRoomUseCase).changeLessonOutletRoom("lesson-1", "room-1");
        }
    }

    @Nested
    @DisplayName("assignEducatorsToLesson")
    class AssignEducators {

        private static final String LESSON_ID = "22222222-2222-2222-2222-222222222222";

        @Test
        @DisplayName("Should throw LessonConflictException(EDUCATOR) and not save links when an educator has a conflict")
        void rejectsOnConflict() {
            Lesson lesson = Lesson.builder()
                    .id(LESSON_ID).tenantId("tenant-1")
                    .lessonStartTimestamptz(Instant.parse("2026-06-10T09:00:00Z"))
                    .lessonEndTimestamptz(Instant.parse("2026-06-10T10:00:00Z"))
                    .build();
            when(lessonRepository.findById(UUID.fromString(LESSON_ID))).thenReturn(lesson);
            when(detectLessonConflictsUseCase.detectEducatorConflicts(
                    "tenant-1", "educator-1",
                    lesson.getLessonStartTimestamptz(), lesson.getLessonEndTimestamptz(),
                    LESSON_ID))
                    .thenReturn(List.of("conflict-lesson"));

            assertThatThrownBy(() -> lessonService.assignEducatorsToLesson(
                    LESSON_ID, List.of("educator-1")))
                    .isInstanceOf(LessonConflictException.class);

            verify(addEducatorsToLessonUseCase, never()).addEducatorsToLessonUseCase(any(), any(), any());
        }

        @Test
        @DisplayName("Should delegate to use case when no conflicts")
        void happyPath() {
            Lesson lesson = Lesson.builder()
                    .id(LESSON_ID).tenantId("tenant-1")
                    .lessonStartTimestamptz(Instant.parse("2026-06-10T09:00:00Z"))
                    .lessonEndTimestamptz(Instant.parse("2026-06-10T10:00:00Z"))
                    .build();
            when(lessonRepository.findById(UUID.fromString(LESSON_ID))).thenReturn(lesson);
            when(educatorRepository.findUserIdsByEducatorIds(List.of("educator-1")))
                    .thenReturn(java.util.Map.of("educator-1", "user-1"));

            lessonService.assignEducatorsToLesson(LESSON_ID, List.of("educator-1"));

            verify(addEducatorsToLessonUseCase).addEducatorsToLessonUseCase(
                    LESSON_ID, List.of("educator-1"), "tenant-1");
            verify(relationshipService).writeRelationships(any());
        }
    }

    @Nested
    @DisplayName("assignStudentsToLesson")
    class AssignStudents {

        private static final String LESSON_ID = "11111111-1111-1111-1111-111111111111";

        @Test
        @DisplayName("Should throw LessonConflictException(STUDENT) and not save links when a student has a conflict")
        void rejectsOnConflict() {
            Lesson lesson = Lesson.builder()
                    .id(LESSON_ID).tenantId("tenant-1")
                    .lessonStartTimestamptz(Instant.parse("2026-06-10T09:00:00Z"))
                    .lessonEndTimestamptz(Instant.parse("2026-06-10T10:00:00Z"))
                    .build();
            when(lessonRepository.findById(UUID.fromString(LESSON_ID))).thenReturn(lesson);
            when(detectLessonConflictsUseCase.detectStudentConflicts(
                    "tenant-1", "student-1",
                    lesson.getLessonStartTimestamptz(), lesson.getLessonEndTimestamptz(),
                    LESSON_ID))
                    .thenReturn(List.of("conflict-lesson"));

            assertThatThrownBy(() -> lessonService.assignStudentsToLesson(
                    LESSON_ID, List.of("student-1")))
                    .isInstanceOf(LessonConflictException.class);

            verify(addStudentsToLessonUseCase, never()).addStudentsToLesson(any(), any(), any());
        }

        @Test
        @DisplayName("Should delegate to use case when no conflicts")
        void happyPath() {
            Lesson lesson = Lesson.builder()
                    .id(LESSON_ID).tenantId("tenant-1")
                    .lessonStartTimestamptz(Instant.parse("2026-06-10T09:00:00Z"))
                    .lessonEndTimestamptz(Instant.parse("2026-06-10T10:00:00Z"))
                    .build();
            when(lessonRepository.findById(UUID.fromString(LESSON_ID))).thenReturn(lesson);
            when(studentRepository.findUserIdsByStudentIds(List.of("student-1")))
                    .thenReturn(java.util.Map.of("student-1", "user-1"));

            lessonService.assignStudentsToLesson(LESSON_ID, List.of("student-1"));

            verify(addStudentsToLessonUseCase).addStudentsToLesson(
                    LESSON_ID, List.of("student-1"), "tenant-1");
            verify(relationshipService).writeRelationships(any());
        }
    }
}
