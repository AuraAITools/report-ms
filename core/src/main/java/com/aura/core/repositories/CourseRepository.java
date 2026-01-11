package com.aura.core.repositories;

import com.aura.core.entities.courses.Course;

public interface CourseRepository {
    Course save(Course course);

    Course findById(String id);

}
