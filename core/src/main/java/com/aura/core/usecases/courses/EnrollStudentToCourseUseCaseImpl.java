package com.aura.core.usecases.courses;

import com.aura.core.entities.courses.Course;
import com.aura.core.repositories.CourseRepository;
import com.aura.core.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EnrollStudentToCourseUseCaseImpl implements EnrollStudentToCourseUseCase {
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Override
    public Course enroll(String studentId, String courseId, String tenantId) {
        return studentRepository.createStudentCourseLink(studentId, courseId, tenantId); // TODO: throws student is already enrolled in course
    }
}
