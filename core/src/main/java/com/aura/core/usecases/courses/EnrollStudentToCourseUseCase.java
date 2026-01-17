package com.aura.core.usecases.courses;

import com.aura.core.entities.courses.Course;

public interface EnrollStudentToCourseUseCase {
    Course enroll(String studentId, String courseId, String tenantId);
}
