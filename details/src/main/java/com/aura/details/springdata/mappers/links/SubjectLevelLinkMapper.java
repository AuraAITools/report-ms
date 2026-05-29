package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.SubjectLevelLink;
import com.aura.details.springdata.models.levels.LevelModel;
import com.aura.details.springdata.models.links.SubjectLevelLinkModel;
import com.aura.details.springdata.models.subjects.SubjectModel;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SubjectLevelLinkMapper {
    SubjectLevelLinkMapper INSTANCE = Mappers.getMapper(SubjectLevelLinkMapper.class);

    @Mapping(source = "levelId", target = "level", qualifiedByName = "levelRef")
    @Mapping(source = "subjectId", target = "subject", qualifiedByName = "subjectRef")
    SubjectLevelLinkModel toModelEntity(SubjectLevelLink domain);

    @Mapping(source = "level.id", target = "levelId")
    @Mapping(source = "subject.id", target = "subjectId")
    SubjectLevelLink toDomain(SubjectLevelLinkModel table);

    @Named("levelRef")
    default LevelModel levelRef(String levelId) {
        if (levelId == null) {
            return null;
        }
        return LevelModel.builder().id(UUID.fromString(levelId)).build();
    }

    @Named("subjectRef")
    default SubjectModel subjectRef(String subjectId) {
        if (subjectId == null) {
            return null;
        }
        return SubjectModel.builder().id(UUID.fromString(subjectId)).build();
    }
}
