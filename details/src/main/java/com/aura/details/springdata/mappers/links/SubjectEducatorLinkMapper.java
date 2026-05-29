package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.SubjectEducatorLink;
import com.aura.details.springdata.models.educators.EducatorModel;
import com.aura.details.springdata.models.links.SubjectEducatorLinkModel;
import com.aura.details.springdata.models.subjects.SubjectModel;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SubjectEducatorLinkMapper {
    SubjectEducatorLinkMapper INSTANCE = Mappers.getMapper(SubjectEducatorLinkMapper.class);

    @Mapping(source = "educatorId", target = "educator", qualifiedByName = "educatorRef")
    @Mapping(source = "subjectId", target = "subject", qualifiedByName = "subjectRef")
    SubjectEducatorLinkModel toModelEntity(SubjectEducatorLink domain);

    @Mapping(source = "educator.id", target = "educatorId")
    @Mapping(source = "subject.id", target = "subjectId")
    SubjectEducatorLink toDomain(SubjectEducatorLinkModel table);

    @Named("educatorRef")
    default EducatorModel educatorRef(String educatorId) {
        if (educatorId == null) {
            return null;
        }
        return EducatorModel.builder().id(UUID.fromString(educatorId)).build();
    }

    @Named("subjectRef")
    default SubjectModel subjectRef(String subjectId) {
        if (subjectId == null) {
            return null;
        }
        return SubjectModel.builder().id(UUID.fromString(subjectId)).build();
    }
}
