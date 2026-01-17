package com.aura.core.usecases.accounts;

import com.aura.core.entities.students.Student;

public interface CreateStudentsInParentAccountUseCase {
    Student createStudent(String accountId, Student student);
}
