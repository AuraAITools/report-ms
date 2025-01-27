package com.reportai.www.reportapi.mappers;

import com.reportai.www.reportapi.api.v1.accounts.requests.CreateStudentDTO;
import com.reportai.www.reportapi.api.v1.accounts.responses.CreateStudentResponseDTO;
import com.reportai.www.reportapi.entities.Student;
import java.util.UUID;

public class StudentMappers {

    // level id is not included
    public static Student convert(CreateStudentDTO from, UUID id) {
        return Student
                .builder()
                .name(from.getName())
                .email(from.getEmail())
                .currentSchool(from.getCurrentSchool())
                .dateOfBirth(from.getDateOfBirth())
                .tenantId(id.toString())
                .build();
    }

    public static CreateStudentResponseDTO convert(Student from) {
        return CreateStudentResponseDTO
                .builder()
                .id(from.getId().toString())
                .name(from.getName())
                .email(from.getEmail())
                .currentSchool(from.getCurrentSchool())
                .dateOfBirth(from.getDateOfBirth())
                .build();

    }
}
