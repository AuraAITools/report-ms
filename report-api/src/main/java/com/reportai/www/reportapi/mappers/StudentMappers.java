package com.reportai.www.reportapi.mappers;

import com.reportai.www.reportapi.api.v1.accounts.requests.CreateStudentDTO;
import com.reportai.www.reportapi.api.v1.accounts.responses.CreateStudentResponseDTO;
import com.reportai.www.reportapi.api.v1.accounts.responses.StudentClientResponse;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.entities.personas.StudentClientPersona;
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
                .relationship(from.getStudentClientPersona().getRelationship())
                .courses(from.getCourses().stream().map(CourseMappers::convert).toList())
                .level(LevelMappers.convert(from.getLevel()))
                .contact(from.getStudentClientPersona().getAccount().getContact())
                .outlets(from.getOutlets().stream().map(OutletMappers::convert).toList())
                .build();
    }

    public static StudentClientResponse convert(StudentClientPersona from) {
        return StudentClientResponse
                .builder()
                .id(from.getId().toString())
                .firstName(from.getAccount().getFirstName())
                .lastName(from.getAccount().getLastName())
                .email(from.getAccount().getEmail())
                .contact(from.getAccount().getContact())
                .relationship(from.getRelationship())
                .students(from.getStudents().stream().map(StudentMappers::convert).toList())
                .build();
    }
}
