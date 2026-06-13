package com.aura.core.usecases.courses;

import com.aura.core.entities.courses.Course;
import com.aura.core.exceptions.ResourceNotFoundException;
import com.aura.core.repositories.EducatorRepository;
import lombok.RequiredArgsConstructor;


import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public class TagEducatorToCourseUseCaseImpl implements TagEducatorToCourseUseCase {
    private final EducatorRepository educatorRepository;

    @Override
    public Course tag(String educatorId, String courseId) {
        return ofNullable(educatorRepository.findById(educatorId))
                .map(edu -> educatorRepository.createEducatorCourseLink(educatorId, courseId, edu.getTenantId()))
                .orElseThrow(() -> new ResourceNotFoundException("Educator", educatorId));
    }
}
