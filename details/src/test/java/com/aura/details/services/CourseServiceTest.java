package com.aura.details.services;

import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.entities.courses.Course;
import com.aura.core.repositories.EducatorRepository;
import com.aura.core.repositories.StudentRepository;
import com.aura.core.repositories.SubjectRepository;
import com.aura.core.usecases.courses.CreateCourseUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DisplayName("Course service")
@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock private CreateCourseUseCase createCourseUseCase;
    @Mock private RelationshipAuthorizationService relationshipService;
    @Mock private SubjectRepository subjectRepository;
    @Mock private EducatorRepository educatorRepository;
    @Mock private StudentRepository studentRepository;

    @InjectMocks private CourseService courseService;

    @DisplayName("Should create course and write parent_outlet relationship")
    @Test
    void createCourseInOutlet_writesParentOutletRelationship() {
        String outletId = "outlet-456";
        Course input = Course.builder().name("Test Course").build();
        Course created = Course.builder().id("course-123").name("Test Course").tenantId("tenant-789").build();
        when(createCourseUseCase.createCourseInOutlet(any(), eq(outletId))).thenReturn(created);

        Course result = courseService.createCourseInOutlet(input, outletId, List.of(), List.of(), List.of());

        assertThat(result).isEqualTo(created);
        verify(relationshipService).writeRelationship(
                "course", "course-123", "parent_outlet", "outlet", "outlet-456");
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

    @DisplayName("Should create educator-course links and write SpiceDB educator relationships")
    @Test
    void createCourseInOutlet_createsEducatorLinksAndSpiceDbRelationships() {
        Course input = Course.builder().name("Test").build();
        Course created = Course.builder().id("course-1").tenantId("tenant-1").build();
        when(createCourseUseCase.createCourseInOutlet(any(), any())).thenReturn(created);
        when(educatorRepository.findUserIdsByEducatorIds(List.of("edu-1", "edu-2")))
                .thenReturn(Map.of("edu-1", "user-aaa", "edu-2", "user-bbb"));

        courseService.createCourseInOutlet(input, "outlet-1",
                List.of(), List.of("edu-1", "edu-2"), List.of());

        verify(educatorRepository).createEducatorCourseLink("edu-1", "course-1", "tenant-1");
        verify(educatorRepository).createEducatorCourseLink("edu-2", "course-1", "tenant-1");
        verify(relationshipService).writeRelationship("course", "course-1", "educator", "user", "user-aaa");
        verify(relationshipService).writeRelationship("course", "course-1", "educator", "user", "user-bbb");
    }

    @DisplayName("Should create student-course links and write SpiceDB viewer relationships")
    @Test
    void createCourseInOutlet_createsStudentLinksAndSpiceDbRelationships() {
        Course input = Course.builder().name("Test").build();
        Course created = Course.builder().id("course-1").tenantId("tenant-1").build();
        when(createCourseUseCase.createCourseInOutlet(any(), any())).thenReturn(created);
        when(studentRepository.findUserIdsByStudentIds(List.of("stu-1")))
                .thenReturn(Map.of("stu-1", "user-xxx"));

        courseService.createCourseInOutlet(input, "outlet-1",
                List.of(), List.of(), List.of("stu-1"));

        verify(studentRepository).createStudentCourseLink("stu-1", "course-1", "tenant-1");
        verify(relationshipService).writeRelationship("course", "course-1", "viewer", "user", "user-xxx");
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
}
