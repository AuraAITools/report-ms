package com.aura.core.usecases.students;

import com.aura.core.entities.levels.Level;
import com.aura.core.entities.students.Student;
import com.aura.core.repositories.StudentRepository;

public class TagStudentToLevelUseCaseImpl implements TagStudentToLevelUseCase {

    private final StudentRepository studentRepository;

    public TagStudentToLevelUseCaseImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Level tagStudentToLevel(String studentId, String levelName) {
        Student student = studentRepository.findById(studentId);
        return studentRepository.tagStudentToLevelByName(student.getId(), levelName, student.getTenantId());
    }
}
