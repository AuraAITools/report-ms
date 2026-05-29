package com.aura.core.usecases.courses;

import com.aura.core.entities.courses.Course;
import com.aura.core.repositories.CourseRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class CourseQueryUseCaseImpl implements CourseQueryUseCase {
    private final CourseRepository courseRepository;

    @Override
    public @Nullable Course findById(String id) {
        return courseRepository.findById(id);
    }

    @Override
    public List<Course> findAllByOutletId(String outletId) {
        return courseRepository.findAllByOutletId(outletId);
    }
}
