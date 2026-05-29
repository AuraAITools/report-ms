package com.aura.core.usecases.courses;

import com.aura.core.repositories.CourseRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteCourseUseCaseImpl implements DeleteCourseUseCase {
    private final CourseRepository courseRepository;

    @Override
    public void deleteById(String id) {
        courseRepository.deleteById(id);
    }
}
