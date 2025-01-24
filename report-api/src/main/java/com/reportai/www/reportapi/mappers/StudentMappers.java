package com.reportai.www.reportapi.mappers;

import com.reportai.www.reportapi.api.v1.students.requests.CreateStudentDTO;
import com.reportai.www.reportapi.entities.Level;
import com.reportai.www.reportapi.entities.Student;

public class StudentMappers {

    public static Student convert(CreateStudentDTO from) {
        return Student
                .builder()
                .name(from.getName())
                .email(from.getEmail())
                .level(Level.builder().name(from.getCurrentLevel()).build())
                .currentSchool(from.getCurrentSchool())
                .dateOfBirth(from.getDateOfBirth())
                .build();
    }

//    public static List<Student> convert(CreateClientAccountDTO from) {
//        return from.getStudents()
//                .stream()
//                .map(StudentMappers::convert)
//                .collect(toList());
//    }

}
