package com.aura.core.usecases.courses;

import com.aura.core.entities.courses.Course;
import com.aura.core.repositories.EducatorRepository;


import static java.util.Optional.ofNullable;

public class TagEducatorToCourseUseCaseImpl implements TagEducatorToCourseUseCase {
    private final EducatorRepository educatorRepository;

    public TagEducatorToCourseUseCaseImpl(EducatorRepository educatorRepository) {
        this.educatorRepository = educatorRepository;
    }

    @Override
    public Course tag(String educatorId, String courseId) {
        return ofNullable(educatorRepository.findById(educatorId))
                .map(edu -> educatorRepository.createEducatorCourseLink(educatorId, courseId, edu.getTenantId()))
                .orElseThrow(() -> new RuntimeException("educator not found"));
    }
}
