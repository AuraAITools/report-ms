package com.aura.core.usecases.courses;

import com.aura.core.entities.courses.Course;
import com.aura.core.entities.students.Student;
import com.aura.core.repositories.CourseRepository;
import com.aura.core.repositories.StudentRepository;
import java.util.Optional;


import static java.util.Optional.ofNullable;

public class EnrollStudentToCourseUseCaseImpl implements EnrollStudentToCourseUseCase {
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public EnrollStudentToCourseUseCaseImpl(StudentRepository studentRepository, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public Course enroll(String studentId, String courseId) {
        Optional<Student> nullableStudent = ofNullable(studentRepository.findById(studentId));
        Optional<Course> nullableCourse = ofNullable(courseRepository.findById(courseId));

        if (nullableStudent.isEmpty()) throw new RuntimeException("student does not exist");
        if (nullableCourse.isEmpty()) throw new RuntimeException("course does not exitst");

        return studentRepository.createStudentCourseLink(studentId, courseId); // throws student is already enrolled in course
    }
}
