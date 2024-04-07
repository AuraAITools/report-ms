package com.reportai.www.reportapi.dtos.requests;

import com.reportai.www.reportapi.dtos.DTO;
import com.reportai.www.reportapi.entities.Student;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateStudentRequestBody implements DTO<Student> {

    @NotEmpty(message = "name is a mandatory field")
    public String name;

    @NotNull(message = "user is a mandatory field")
    public CreateUserRequestBody user;

    @Override
    public Student toEntity() {
        return Student.builder()
                .user(user.toEntity())
                .name(name)
                .build();
    }

    @Override
    public <R extends DTO<Student>> R of(Student entity) {
        return null;
    }
}
