package com.aura.details.springdata.mappers.students;

import com.aura.core.entities.students.Student;
import com.aura.details.springdata.mappers.levels.LevelMapper;
import com.aura.details.springdata.mappers.schools.SchoolMapper;
import com.aura.details.springdata.tables.students.StudentTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {SchoolMapper.class, LevelMapper.class})
public interface StudentMapper {
    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    @Mapping(target = "school", ignore = true)
    @Mapping(target = "level", ignore = true)
    @Mapping(target = "subjectStudentLinks", ignore = true)
    @Mapping(target = "testResults", ignore = true)
    @Mapping(target = "studentLessonLinks", ignore = true)
    @Mapping(target = "accountStudentLinks", ignore = true)
    @Mapping(target = "studentCourseLinks", ignore = true)
    @Mapping(target = "studentOutletLinks", ignore = true)
    Student toDomainEntity(StudentTable studentTable);

    @Mapping(target = "school", ignore = true)
    @Mapping(target = "level", ignore = true)
    @Mapping(target = "subjectStudentLinks", ignore = true)
    @Mapping(target = "testResults", ignore = true)
    @Mapping(target = "studentLessonLinks", ignore = true)
    @Mapping(target = "accountStudentLinks", ignore = true)
    @Mapping(target = "studentCourseLinks", ignore = true)
    @Mapping(target = "studentOutletLinks", ignore = true)
    StudentTable toTableEntity(Student student);

}

