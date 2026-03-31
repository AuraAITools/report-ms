package com.aura.core.usecases.students;

import com.aura.core.entities.schools.School;
import com.aura.core.entities.students.Student;
import com.aura.core.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TagStudentToSchoolUseCaseImpl implements TagStudentToSchoolUseCase {
    private final StudentRepository studentRepository;

    @Override
    public School tagStudentToSchool(String studentId, String schoolName) {
        Student student = studentRepository.findById(studentId);
        return studentRepository.tagStudentToSchool(student.getId(), schoolName, student.getTenantId());
    }
}
