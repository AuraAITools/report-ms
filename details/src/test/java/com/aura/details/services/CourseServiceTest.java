package com.aura.details.services;

import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.entities.courses.Course;
import com.aura.core.usecases.courses.CreateCourseUseCase;
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

@DisplayName("Course service")
@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock private CreateCourseUseCase createCourseUseCase;
    @Mock private RelationshipAuthorizationService relationshipService;

    @InjectMocks private CourseService courseService;

    @DisplayName("Should create course and write parent_outlet relationship")
    @Test
    void createCourseInOutlet_writesParentOutletRelationship() {
        String outletId = "outlet-456";
        Course input = Course.builder().name("Test Course").build();
        Course created = Course.builder().id("course-123").name("Test Course").tenantId("tenant-789").build();
        when(createCourseUseCase.createCourseInOutlet(any(), eq(outletId))).thenReturn(created);

        Course result = courseService.createCourseInOutlet(input, outletId);

        assertThat(result).isEqualTo(created);
        verify(relationshipService).writeRelationship(
                "course", "course-123", "parent_outlet", "outlet", "outlet-456");
    }
}
