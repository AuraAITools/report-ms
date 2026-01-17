package com.aura.core.usecases.accounts;

import com.aura.core.entities.students.Student;
import com.aura.core.repositories.AccountRepository;
import com.aura.core.repositories.StudentRepository;

public class CreateStudentsInParentAccountUseCaseImpl implements CreateStudentsInParentAccountUseCase {

    private final StudentRepository studentRepository;
    private final AccountRepository accountRepository;

    public CreateStudentsInParentAccountUseCaseImpl(StudentRepository studentRepository, AccountRepository accountRepository) {
        this.studentRepository = studentRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public Student createStudent(String accountId, Student student) {
        Student createdStudent = studentRepository.save(student);
        accountRepository.linkAccountToStudent(accountId, createdStudent.getId());
        return createdStudent;
    }
}
