package com.aura.app.controllers.graphql.v1.courses;

import com.aura.app.codegen.types.CourseResponse;
import com.aura.app.codegen.types.CreateCourseInput;
import com.aura.app.mappers.CourseMapper;
import com.aura.core.auth.User;
import com.aura.core.entities.courses.Course;
import com.aura.details.services.CourseService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;


import static com.aura.app.codegen.DgsConstants.MUTATION.CREATECOURSE_INPUT_ARGUMENT.CreateCourseInput;
import static com.aura.app.codegen.DgsConstants.MUTATION.CreateCourse;

@Controller
public class CourseGqlController {
    private final CourseService courseService;

    public CourseGqlController(CourseService courseService) {
        this.courseService = courseService;
    }

    @MutationMapping(CreateCourse)
    public CourseResponse createCourse(@Argument(name = CreateCourseInput) CreateCourseInput createCourseInput, @ContextValue(name = "user") User user) {
        Course newCourse = CourseMapper.INSTANCE.toDomainEntity(createCourseInput, user.getCurrentTenantAccessed());
        Course createdCourse = courseService.createCourseInOutlet(newCourse);
        return CourseMapper.INSTANCE.toDto(createdCourse);
    }
}
