package com.aura.app.mappers;

import com.aura.app.codegen.types.CreateSubjectInput;
import com.aura.app.codegen.types.SubjectResponse;
import com.aura.core.entities.subjects.Subject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {DateToEpochMapper.class})
public interface SubjectMapper {

    SubjectMapper INSTANCE = Mappers.getMapper(SubjectMapper.class);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "topicSubjectLinks", ignore = true)
    @Mapping(target = "testResults", ignore = true)
    @Mapping(target = "subjectTestGroupLinks", ignore = true)
    @Mapping(target = "subjectStudentLinks", ignore = true)
    @Mapping(target = "subjectLevelLinks", ignore = true)
    @Mapping(target = "subjectEducatorLinks", ignore = true)
    @Mapping(target = "subjectCourseLinks", ignore = true)
    @Mapping(target = "lessons", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Subject toDomain(CreateSubjectInput createSubjectInput, String tenantId);

    SubjectResponse toDto(Subject subject);
}
