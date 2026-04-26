package com.aura.details.services;

import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.entities.courses.Course;
import com.aura.core.usecases.courses.CreateCourseUseCase;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CourseService {

    private final CreateCourseUseCase createCourseUseCase;
    private final RelationshipAuthorizationService relationshipService;

    @Transactional
    public Course createCourseInOutlet(Course course, String outletId) {
        Course created = createCourseUseCase.createCourseInOutlet(course, outletId);
        relationshipService.writeRelationship("course", created.getId(),
                "parent_outlet", "outlet", outletId);
        return created;
    }
}
