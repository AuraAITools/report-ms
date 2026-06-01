package com.aura.app.mappers;

import com.aura.app.codegen.types.CreateSubjectInput;
import com.aura.app.codegen.types.SubjectResponse;
import com.aura.app.codegen.types.SubjectResponseWithTopics;
import com.aura.core.entities.subjects.Subject;
import com.aura.core.entities.topics.Topic;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {DateToEpochMapper.class, TopicMapper.class, AccountMinimalDetailMapper.class})
public interface SubjectMapper {

    SubjectMapper INSTANCE = Mappers.getMapper(SubjectMapper.class);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(source = "institutionId", target = "tenantId")
    Subject toDomain(CreateSubjectInput createSubjectInput);

    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "toMinimal")
    @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "toMinimal")
    SubjectResponse toDto(Subject subject);

    @Mapping(target = "createdBy", source = "subject.createdBy", qualifiedByName = "toMinimal")
    @Mapping(target = "updatedBy", source = "subject.updatedBy", qualifiedByName = "toMinimal")
    SubjectResponseWithTopics toDto(Subject subject, List<Topic> topics);
}
