package com.aura.details.springdata.mappers.subjects;

import com.aura.core.entities.subjects.Subject;
import com.aura.details.springdata.tables.subjects.SubjectTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SubjectMapper {
    SubjectMapper INSTANCE = Mappers.getMapper(SubjectMapper.class);

    @Mapping(target = "subjectCourseLinks", ignore = true)
    @Mapping(target = "lessons", ignore = true)
    @Mapping(target = "subjectStudentLinks", ignore = true)
    @Mapping(target = "subjectEducatorLinks", ignore = true)
    @Mapping(target = "subjectTestGroupLinks", ignore = true)
    @Mapping(target = "testResults", ignore = true)
    @Mapping(target = "subjectLevelLinks", ignore = true)
    @Mapping(target = "topicSubjectLinks", ignore = true)
    Subject toDomainEntity(SubjectTable subjectTable);

    @Mapping(target = "subjectCourseLinks", ignore = true)
    @Mapping(target = "lessons", ignore = true)
    @Mapping(target = "subjectStudentLinks", ignore = true)
    @Mapping(target = "subjectEducatorLinks", ignore = true)
    @Mapping(target = "subjectTestGroupLinks", ignore = true)
    @Mapping(target = "testResults", ignore = true)
    @Mapping(target = "subjectLevelLinks", ignore = true)
    @Mapping(target = "topicSubjectLinks", ignore = true)
    @Mapping(target = "Id", source = "id")
    SubjectTable toTableEntity(Subject subject);
}

