package com.aura.details.services;

import com.aura.core.clients.GroupAuthorizationService;
import com.aura.core.clients.ResourceAuthorizationService;
import com.aura.core.entities.courses.Course;
import com.aura.core.usecases.courses.CreateCourseUseCase;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CreateCourseUseCase createCourseUseCase;
    @Mock
    private ResourceAuthorizationService resourceAuthorizationService;
    @Mock
    private GroupAuthorizationService groupAuthorizationService;

    private CourseService courseService;

    @BeforeEach
    void setUp() {
        courseService = new CourseService(createCourseUseCase, resourceAuthorizationService, groupAuthorizationService);
    }

    @Test
    void createCourseInOutlet_registersUmaResourceAndReturnsCreatedCourse() {
        String outletId = "outlet-456";
        Course input = Course.builder().name("Test Course").build();
        Course created = Course.builder().id("course-123").name("Test Course").tenantId("tenant-789").build();
        when(createCourseUseCase.createCourseInOutlet(any(), any())).thenReturn(created);

        Course result = courseService.createCourseInOutlet(input, outletId);

        assertThat(result).isEqualTo(created);
        verify(createCourseUseCase).createCourseInOutlet(input, outletId);

        // Verify UMA resource registration
        ArgumentCaptor<String> nameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> typeCaptor = ArgumentCaptor.forClass(String.class);
        @SuppressWarnings("unchecked")
        ArgumentCaptor<Set<String>> scopesCaptor = ArgumentCaptor.forClass(Set.class);

        verify(resourceAuthorizationService).registerResource(
                nameCaptor.capture(), typeCaptor.capture(), any(), scopesCaptor.capture());

        assertThat(nameCaptor.getValue()).isEqualTo("course:course-123");
        assertThat(typeCaptor.getValue()).isEqualTo("urn:aura:course");
        assertThat(scopesCaptor.getValue()).containsExactlyInAnyOrder("view", "manage", "delegate");

        // Verify group creation
        verify(groupAuthorizationService).createGroup("course:course-123:admin");

        // Verify group policy creation
        verify(groupAuthorizationService).createGroupPolicy(
                "course-course-123-admin-policy", "course:course-123:admin");

        // Verify resource permission links both course admin and outlet admin policies
        @SuppressWarnings("unchecked")
        ArgumentCaptor<Set<String>> policiesCaptor = ArgumentCaptor.forClass(Set.class);
        verify(groupAuthorizationService).createResourcePermission(
                any(), any(), any(), policiesCaptor.capture());
        assertThat(policiesCaptor.getValue()).containsExactlyInAnyOrder(
                "course-course-123-admin-policy",
                "outlet-outlet-456-admin-policy"
        );
    }
}
