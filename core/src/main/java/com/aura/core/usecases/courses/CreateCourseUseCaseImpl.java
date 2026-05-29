package com.aura.core.usecases.courses;

import com.aura.core.entities.courses.Course;
import com.aura.core.exceptions.TechnicalException;
import com.aura.core.repositories.CourseRepository;
import com.aura.core.repositories.OutletRepository;
import lombok.RequiredArgsConstructor;


import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public class CreateCourseUseCaseImpl implements CreateCourseUseCase {
    private final CourseRepository courseRepository;
    private final OutletRepository outletRepository;

    @Override
    public Course createCourseInOutlet(Course course, String outletId) {
        return ofNullable(outletRepository.findById(outletId))
                .map(outlet -> {
                    course.setOutletId(outlet.getId());
                    return courseRepository.save(course);
                })
                .orElseThrow(() -> new TechnicalException("Outlet not found"));
    }
}
