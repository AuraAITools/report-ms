package com.aura.core.usecases.students;

import com.aura.core.entities.levels.Level;
import com.aura.core.entities.students.Student;
import com.aura.core.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TagStudentToLevelUseCaseImpl implements TagStudentToLevelUseCase {
    private final StudentRepository studentRepository;

    @Override
    public Level tagStudentToLevel(String studentId, String levelName) {
        Student student = studentRepository.findById(studentId);
        return studentRepository.tagStudentToLevel(student.getId(), levelName, student.getTenantId());
    }
}
