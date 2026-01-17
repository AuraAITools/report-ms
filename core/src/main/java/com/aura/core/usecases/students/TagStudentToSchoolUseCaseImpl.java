package com.aura.core.usecases.students;

import com.aura.core.entities.schools.School;
import com.aura.core.entities.students.Student;
import com.aura.core.repositories.StudentRepository;

public class TagStudentToSchoolUseCaseImpl implements TagStudentToSchoolUseCase {
    private final StudentRepository studentRepository;

    public TagStudentToSchoolUseCaseImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public School tagStudentToSchool(String studentId, String schoolName) {
        Student student = studentRepository.findById(studentId);
        return studentRepository.tagStudentToSchool(student.getId(), schoolName, student.getTenantId());
    }
}
