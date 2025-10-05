package com.aura.details.mappers.students;

import com.aura.core.entities.students.Student;
import com.aura.details.mappers.levels.LevelMapper;
import com.aura.details.mappers.schools.SchoolMapper;
import com.aura.details.tables.students.StudentTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {SchoolMapper.class, LevelMapper.class})
public interface StudentMapper {
    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    @Mapping(target = "subjectStudentLinks", ignore = true)
    @Mapping(target = "testResults", ignore = true)
    @Mapping(target = "studentLessonLinks", ignore = true)
    @Mapping(target = "accountStudentLinks", ignore = true)
    @Mapping(target = "studentCourseLinks", ignore = true)
    @Mapping(target = "studentOutletLinks", ignore = true)
    Student toDomainEntity(StudentTable studentTable);

    @Mapping(target = "subjectStudentLinks", ignore = true)
    @Mapping(target = "testResults", ignore = true)
    @Mapping(target = "studentLessonLinks", ignore = true)
    @Mapping(target = "accountStudentLinks", ignore = true)
    @Mapping(target = "studentCourseLinks", ignore = true)
    @Mapping(target = "studentOutletLinks", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "Id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    StudentTable toTableEntity(Student student);
}

