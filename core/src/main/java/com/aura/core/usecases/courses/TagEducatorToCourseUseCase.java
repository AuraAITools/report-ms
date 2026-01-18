package com.aura.core.usecases.courses;

import com.aura.core.entities.courses.Course;

public interface TagEducatorToCourseUseCase {
    Course tag(String educatorId, String courseId);
}
