package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.SubjectTestGroupLink;
import com.aura.details.springdata.models.links.SubjectTestGroupLinkModel;
import com.aura.details.springdata.models.subjects.SubjectModel;
import com.aura.details.springdata.models.tests.TestGroupModel;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SubjectTestGroupLinkMapper {
    SubjectTestGroupLinkMapper INSTANCE = Mappers.getMapper(SubjectTestGroupLinkMapper.class);

    @Mapping(source = "subjectId", target = "subject", qualifiedByName = "subjectRef")
    @Mapping(source = "testGroupId", target = "testGroup", qualifiedByName = "testGroupRef")
    SubjectTestGroupLinkModel toModelEntity(SubjectTestGroupLink domain);

    @Mapping(source = "subject.id", target = "subjectId")
    @Mapping(source = "testGroup.id", target = "testGroupId")
    SubjectTestGroupLink toDomain(SubjectTestGroupLinkModel table);

    @Named("subjectRef")
    default SubjectModel subjectRef(String subjectId) {
        if (subjectId == null) {
            return null;
        }
        return SubjectModel.builder().id(UUID.fromString(subjectId)).build();
    }

    @Named("testGroupRef")
    default TestGroupModel testGroupRef(String testGroupId) {
        if (testGroupId == null) {
            return null;
        }
        return TestGroupModel.builder().id(UUID.fromString(testGroupId)).build();
    }
}
