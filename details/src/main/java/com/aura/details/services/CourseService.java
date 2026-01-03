package com.aura.details.services;

import com.aura.core.entities.courses.Course;
import com.aura.core.usecases.courses.CreateCourseUseCase;
import jakarta.transaction.Transactional;

public class CourseService {
    private final CreateCourseUseCase createCourseUseCase;

    public CourseService(CreateCourseUseCase createCourseUseCase) {
        this.createCourseUseCase = createCourseUseCase;
    }

    @Transactional
    public Course createCourseInOutlet(Course course) {
        return createCourseUseCase.createCourseInOutlet(course);
    }
    
}
