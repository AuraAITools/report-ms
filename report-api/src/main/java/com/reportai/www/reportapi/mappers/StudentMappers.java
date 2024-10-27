package com.reportai.www.reportapi.mappers;

import com.reportai.www.reportapi.dtos.requests.CreateClientAccountDTO;
import com.reportai.www.reportapi.dtos.requests.CreateStudentDTO;
import com.reportai.www.reportapi.entities.Student;

import java.util.List;
import static java.util.stream.Collectors.toList;

public class StudentMappers {

    public static Student convert(CreateStudentDTO from) {
        return Student
                .builder()
                .name(from.getName())
                .email(from.getEmail())
                .currentLevel(from.getCurrentLevel())
                .currentSchool(from.getCurrentSchool())
                .dateOfBirth(from.getDateOfBirth())
                .build();
    }

    public static List<Student> convert(CreateClientAccountDTO from) {
        return from.getStudents()
                .stream()
                .map(StudentMappers::convert)
                .collect(toList());
    }

}
