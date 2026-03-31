package com.aura.details.services;

import com.aura.core.entities.courses.Course;
import com.aura.core.usecases.courses.CreateCourseUseCase;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CourseService {
    private final CreateCourseUseCase createCourseUseCase;

    @Transactional
    public Course createCourseInOutlet(Course course, String outletId) {
        return createCourseUseCase.createCourseInOutlet(course, outletId);
    }

}
