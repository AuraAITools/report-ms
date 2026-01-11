package com.aura.core.usecases.students;

import com.aura.core.entities.schools.School;

public interface TagStudentToSchoolUseCase {
    School tagStudentToSchool(String studentId, String schoolName);
}
