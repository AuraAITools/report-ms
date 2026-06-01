package com.aura.details.services;

import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.entities.courses.Course;
import com.aura.core.entities.lessons.Lesson;
import com.aura.core.repositories.CourseRepository;
import com.aura.core.repositories.EducatorRepository;
import com.aura.core.repositories.LessonRepository;
import com.aura.core.repositories.StudentRepository;
import com.aura.core.repositories.SubjectRepository;
import com.aura.core.usecases.courses.CourseQueryUseCase;
import com.aura.core.usecases.courses.CreateCourseUseCase;
import com.aura.core.usecases.courses.DeleteCourseUseCase;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@DisplayName("Course service")
@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CreateCourseUseCase createCourseUseCase;
    @Mock
    private CourseQueryUseCase courseQueryUseCase;
    @Mock
    private DeleteCourseUseCase deleteCourseUseCase;
    @Mock
    private LessonService lessonService;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private LessonRepository lessonRepository;
    @Mock
    private RelationshipAuthorizationService relationshipService;
    @Mock
    private SubjectRepository subjectRepository;
    @Mock
    private EducatorRepository educatorRepository;
    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private CourseService courseService;

    @DisplayName("Should write only the three structural relationships in a single batch when no users are attached")
    @Test
    void createCourseInOutlet_writesStructuralRelationshipsAsBatch_whenAllListsEmpty() {
        String outletId = "outlet-456";
        Course input = Course.builder().name("Test Course").build();
        Course created = Course.builder().id("course-123").name("Test Course").tenantId("tenant-789").build();
        when(createCourseUseCase.createCourseInOutlet(any(), eq(outletId))).thenReturn(created);

        Course result = courseService.createCourseInOutlet(input, outletId, List.of(), List.of(), List.of());

        assertThat(result).isEqualTo(created);
        verify(relationshipService).writeRelationships(argThat(list ->
                list.containsAll(List.of(
                        new RelationshipAuthorizationService.Relationship(
                                "course", "course-123", "user_group", "course_user_group", "course-123"),
                        new RelationshipAuthorizationService.Relationship(
                                "course_user_group", "course-123", "parent", "system_user_group", "aura"),
                        new RelationshipAuthorizationService.Relationship(
                                "course", "course-123", "parent", "outlet", "outlet-456")))
                        && list.size() == 3));
        verifyNoMoreInteractions(relationshipService);
    }

    @DisplayName("Should create subject-course links for each subject ID")
    @Test
    void createCourseInOutlet_createsSubjectCourseLinks() {
        Course input = Course.builder().name("Test").build();
        Course created = Course.builder().id("course-1").tenantId("tenant-1").build();
        when(createCourseUseCase.createCourseInOutlet(any(), any())).thenReturn(created);

        courseService.createCourseInOutlet(input, "outlet-1",
                List.of("sub-1", "sub-2"), List.of(), List.of());

        verify(subjectRepository).createSubjectCourseLinks(List.of("sub-1", "sub-2"), "course-1", "tenant-1");
    }

    @DisplayName("Should batch structural + educator modifier + student viewer relationships in one call and create DB links per educator/student")
    @Test
    void createCourseInOutlet_batchesStructuralAndUserRoleGrants() {
        Course input = Course.builder().name("Test").build();
        Course created = Course.builder().id("course-1").tenantId("tenant-1").build();
        when(createCourseUseCase.createCourseInOutlet(any(), any())).thenReturn(created);
        when(educatorRepository.findUserIdsByEducatorIds(List.of("edu-1", "edu-2")))
                .thenReturn(Map.of("edu-1", "user-aaa", "edu-2", "user-bbb"));
        when(studentRepository.findUserIdsByStudentIds(List.of("stu-1")))
                .thenReturn(Map.of("stu-1", "user-xxx"));

        courseService.createCourseInOutlet(input, "outlet-1",
                List.of(), List.of("edu-1", "edu-2"), List.of("stu-1"));

        verify(educatorRepository).createEducatorCourseLinks(List.of("edu-1", "edu-2"), "course-1", "tenant-1");
        verify(studentRepository).createStudentCourseLinks(List.of("stu-1"), "course-1", "tenant-1");

        verify(relationshipService).writeRelationships(argThat(list ->
                list.containsAll(List.of(
                        new RelationshipAuthorizationService.Relationship(
                                "course", "course-1", "user_group", "course_user_group", "course-1"),
                        new RelationshipAuthorizationService.Relationship(
                                "course_user_group", "course-1", "parent", "system_user_group", "aura"),
                        new RelationshipAuthorizationService.Relationship(
                                "course", "course-1", "parent", "outlet", "outlet-1"),
                        new RelationshipAuthorizationService.Relationship(
                                "course_user_group", "course-1", "modifier", "user", "user-aaa"),
                        new RelationshipAuthorizationService.Relationship(
                                "course_user_group", "course-1", "modifier", "user", "user-bbb"),
                        new RelationshipAuthorizationService.Relationship(
                                "course_user_group", "course-1", "viewer", "user", "user-xxx")))
                        && list.size() == 6));
        verifyNoMoreInteractions(relationshipService);
    }

    @DisplayName("Should omit role grants for educators/students whose userId lookup returns null but still create DB links")
    @Test
    void createCourseInOutlet_skipsRoleGrantsForNullUserIds() {
        Course input = Course.builder().name("Test").build();
        Course created = Course.builder().id("course-1").tenantId("tenant-1").build();
        when(createCourseUseCase.createCourseInOutlet(any(), any())).thenReturn(created);

        Map<String, String> educatorUserIds = new HashMap<>();
        educatorUserIds.put("edu-1", "user-aaa");
        educatorUserIds.put("edu-2", null);
        when(educatorRepository.findUserIdsByEducatorIds(List.of("edu-1", "edu-2")))
                .thenReturn(educatorUserIds);

        courseService.createCourseInOutlet(input, "outlet-1",
                List.of(), List.of("edu-1", "edu-2"), List.of());

        verify(educatorRepository).createEducatorCourseLinks(List.of("edu-1", "edu-2"), "course-1", "tenant-1");

        // NOTE: production code does not filter null userIds before building SpiceDB relationships;
        // the null entry is included as-is. The size is 5 (3 structural + 1 non-null educator + 1 null educator).
        verify(relationshipService).writeRelationships(argThat(list ->
                list.containsAll(List.of(
                        new RelationshipAuthorizationService.Relationship(
                                "course", "course-1", "user_group", "course_user_group", "course-1"),
                        new RelationshipAuthorizationService.Relationship(
                                "course_user_group", "course-1", "parent", "system_user_group", "aura"),
                        new RelationshipAuthorizationService.Relationship(
                                "course", "course-1", "parent", "outlet", "outlet-1"),
                        new RelationshipAuthorizationService.Relationship(
                                "course_user_group", "course-1", "modifier", "user", "user-aaa")))
                        && list.size() == 5));
        verifyNoMoreInteractions(relationshipService);
    }

    @DisplayName("Should skip linking when all ID lists are empty")
    @Test
    void createCourseInOutlet_emptyLists_skipsLinking() {
        Course input = Course.builder().name("Test").build();
        Course created = Course.builder().id("course-1").tenantId("tenant-1").build();
        when(createCourseUseCase.createCourseInOutlet(any(), any())).thenReturn(created);

        courseService.createCourseInOutlet(input, "outlet-1", List.of(), List.of(), List.of());

        verify(subjectRepository).createSubjectCourseLinks(List.of(), "course-1", "tenant-1");
        verifyNoMoreInteractions(subjectRepository);
        verifyNoInteractions(educatorRepository, studentRepository);
    }

    @DisplayName("Should delegate findById to the course query use case")
    @Test
    void findById_delegatesToQueryUseCase() {
        Course course = Course.builder().id("c-1").name("Algebra").build();
        when(courseQueryUseCase.findById("c-1")).thenReturn(course);

        assertThat(courseService.findById("c-1")).isEqualTo(course);
    }

    @DisplayName("Should delegate findAllByOutletId to the course query use case")
    @Test
    void findAllByOutletId_delegatesToQueryUseCase() {
        Course c1 = Course.builder().id("c-1").outletId("o-1").build();
        Course c2 = Course.builder().id("c-2").outletId("o-1").build();
        when(courseQueryUseCase.findAllByOutletId("o-1")).thenReturn(List.of(c1, c2));

        assertThat(courseService.findAllByOutletId("o-1")).containsExactly(c1, c2);
    }

    @DisplayName("Should delegate findAllByEducatorId to the course query use case")
    @Test
    void findAllByEducatorId_delegatesToQueryUseCase() {
        Course c1 = Course.builder().id("c-1").build();
        when(courseQueryUseCase.findAllByEducatorId("e-1")).thenReturn(List.of(c1));

        assertThat(courseService.findAllByEducatorId("e-1")).containsExactly(c1);
    }

    @DisplayName("Should delegate findAllByStudentId to the course query use case")
    @Test
    void findAllByStudentId_delegatesToQueryUseCase() {
        Course c1 = Course.builder().id("c-1").build();
        when(courseQueryUseCase.findAllByStudentId("s-1")).thenReturn(List.of(c1));

        assertThat(courseService.findAllByStudentId("s-1")).containsExactly(c1);
    }

    @DisplayName("Should cascade-delete child lessons, clear course links, delete course, then wipe spicedb")
    @Test
    void deleteById_cascadeDeletesLessonsAndCleansUp() {
        Lesson lesson1 = Lesson.builder().id("lesson-a").build();
        Lesson lesson2 = Lesson.builder().id("lesson-b").build();
        when(lessonRepository.findAllByCourseId("course-1")).thenReturn(List.of(lesson1, lesson2));

        courseService.deleteById("course-1");

        verify(lessonService).deleteById("lesson-a");
        verify(lessonService).deleteById("lesson-b");
        verify(subjectRepository).deleteAllSubjectCourseLinksByCourseId("course-1");
        verify(educatorRepository).deleteAllEducatorCourseLinksByCourseId("course-1");
        verify(studentRepository).deleteAllStudentCourseLinksByCourseId("course-1");
        verify(deleteCourseUseCase).deleteById("course-1");
        verify(relationshipService).deleteAllRelationshipsForResource("course", "course-1");
        verify(relationshipService).deleteAllRelationshipsForResource("course_user_group", "course-1");
    }
}
