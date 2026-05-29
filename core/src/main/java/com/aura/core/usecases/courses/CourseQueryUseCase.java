package com.aura.core.usecases.courses;

import com.aura.core.entities.courses.Course;
import java.util.List;
import org.jspecify.annotations.Nullable;

public interface CourseQueryUseCase {
    @Nullable Course findById(String id);

    List<Course> findAllByOutletId(String outletId);
}
