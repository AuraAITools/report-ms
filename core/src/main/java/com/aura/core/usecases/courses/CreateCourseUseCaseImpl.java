package com.aura.core.usecases.courses;

import com.aura.core.entities.courses.Course;
import com.aura.core.repositories.CourseRepository;
import com.aura.core.repositories.OutletRepository;

public class CreateCourseUseCaseImpl implements CreateCourseUseCase {
    private final CourseRepository courseRepository;
    private final OutletRepository outletRepository;

    public CreateCourseUseCaseImpl(CourseRepository courseRepository, OutletRepository outletRepository) {
        this.courseRepository = courseRepository;
        this.outletRepository = outletRepository;
    }

    @Override
    public Course createCourseInOutlet(Course course) {
        return outletRepository.findById(course.getOutlet().getId()).map(outlet -> {
            course.setOutlet(outlet);
            return courseRepository.save(course);
        }).orElseThrow(() -> new RuntimeException("Outlet not found"));
    }
}
