package com.aura.core.repositories;

import com.aura.core.entities.courses.Course;
import org.jspecify.annotations.Nullable;

public interface CourseRepository {
    Course save(Course course);

    @Nullable Course findById(String id);

}
