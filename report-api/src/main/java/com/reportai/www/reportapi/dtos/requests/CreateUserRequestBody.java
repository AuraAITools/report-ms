package com.reportai.www.reportapi.dtos.requests;


import com.reportai.www.reportapi.dtos.DTO;
import com.reportai.www.reportapi.entities.Educator;
import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.entities.Parent;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.entities.User;
import jakarta.transaction.NotSupportedException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.h2.engine.UserBuilder;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Slf4j
public class CreateUserRequestBody implements DTO<User> {

    public enum accountTypes {
        parent,
        institution,
        educator,
        student
    }

    @NotEmpty(message = "email is a mandatory field")
    private String email;

    @NotEmpty(message = "name is a mandatory field")
    private String name;

    @NotEmpty(message = "no accounts specified")
    private List<accountTypes> accounts;

    @Override
    public User toEntity() {
        User.UserBuilder userBuilder = User.builder()
                .email(email)
                .name(name);

        accounts.forEach(acc -> {
            log.info(acc.toString());
            switch (acc){
                case accountTypes.parent:
                    userBuilder.parents(
                            List.of(Parent.builder()
                                            .name(name)
                                            .build()));
                    break;
                case accountTypes.student:
                    userBuilder.students(
                            List.of(Student.builder()
                                    .name(name)
                                    .build()));
                    break;
                case accountTypes.educator:
                    userBuilder.educators(
                            List.of(Educator.builder()
                                    .name(name)
                                    .build()));
                case accountTypes.institution:
                    userBuilder.institutions(
                            List.of(Institution.builder()
                                    .name(name)
                                    .build()));
                    break;
                default:
                    // do nothing
            }
        });
        return userBuilder.build();
    }

    // FIXME: currently this method does not need to be supported
    @Override
    public <R extends DTO<User>> R of(User entity) {
        return null;
    }
}

