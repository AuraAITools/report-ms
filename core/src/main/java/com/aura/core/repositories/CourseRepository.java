package com.aura.core.repositories;

import com.aura.core.entities.courses.Course;
import java.util.Optional;
import java.util.UUID;

public interface CourseRepository {
    Course save(Course course);

    Optional<Course> findById(UUID id);
}
