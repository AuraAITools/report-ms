package com.aura.core.usecases.accounts;

import com.aura.core.entities.students.Student;
import com.aura.core.repositories.AccountRepository;
import com.aura.core.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateStudentsInParentAccountUseCaseImpl implements CreateStudentsInParentAccountUseCase {
    private final StudentRepository studentRepository;
    private final AccountRepository accountRepository;

    @Override
    public Student createStudent(String accountId, Student student) {
        Student createdStudent = studentRepository.save(student);
        accountRepository.linkAccountToStudent(accountId, createdStudent.getId());
        return createdStudent;
    }
}
