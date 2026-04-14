package com.aura.core.usecases.courses;

import com.aura.core.entities.courses.Course;

public interface CreateCourseUseCase {
    Course createCourseInOutlet(Course course, String outletId);
}
