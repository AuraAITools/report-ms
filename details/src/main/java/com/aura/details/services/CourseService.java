package com.aura.details.services;

import com.aura.core.clients.GroupAuthorizationService;
import com.aura.core.clients.ResourceAuthorizationService;
import com.aura.core.entities.courses.Course;
import com.aura.core.usecases.courses.CreateCourseUseCase;
import jakarta.transaction.Transactional;
import java.util.Set;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CourseService {
    private static final String COURSE_RESOURCE_TYPE = "urn:aura:course";

    private final CreateCourseUseCase createCourseUseCase;
    private final ResourceAuthorizationService resourceAuthorizationService;
    private final GroupAuthorizationService groupAuthorizationService;

    @Transactional
    public Course createCourseInOutlet(Course course, String outletId) {
        Course created = createCourseUseCase.createCourseInOutlet(course, outletId);

        String courseId = created.getId();
        String resourceName = "course:" + courseId;
        String courseGroupName = "course:" + courseId + ":admin";
        String coursePolicyName = "course-" + courseId + "-admin-policy";
        String permissionName = "course-" + courseId + "-permission";
        String outletPolicyName = "outlet-" + outletId + "-admin-policy";

        // 1. Register UMA resource
        resourceAuthorizationService.registerResource(
                resourceName,
                COURSE_RESOURCE_TYPE,
                null,
                Set.of("view", "manage", "delegate")
        );

        // 2. Create course admin group
        groupAuthorizationService.createGroup(courseGroupName);

        // 3. Create course admin group policy
        groupAuthorizationService.createGroupPolicy(coursePolicyName, courseGroupName);

        // 4. Create resource permission linking BOTH course admin AND outlet admin policies
        groupAuthorizationService.createResourcePermission(
                permissionName,
                resourceName,
                Set.of("view", "manage", "delegate"),
                Set.of(coursePolicyName, outletPolicyName)
        );

        return created;
    }

}
