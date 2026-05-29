package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.LevelEducatorLink;
import com.aura.details.springdata.models.educators.EducatorModel;
import com.aura.details.springdata.models.levels.LevelModel;
import com.aura.details.springdata.models.links.LevelEducatorLinkModel;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LevelEducatorLinkMapper {
    LevelEducatorLinkMapper INSTANCE = Mappers.getMapper(LevelEducatorLinkMapper.class);

    @Mapping(source = "educatorId", target = "educator", qualifiedByName = "educatorRef")
    @Mapping(source = "levelId", target = "level", qualifiedByName = "levelRef")
    LevelEducatorLinkModel toModelEntity(LevelEducatorLink domain);

    @Mapping(source = "educator.id", target = "educatorId")
    @Mapping(source = "level.id", target = "levelId")
    LevelEducatorLink toDomain(LevelEducatorLinkModel table);

    @Named("educatorRef")
    default EducatorModel educatorRef(String educatorId) {
        if (educatorId == null) {
            return null;
        }
        return EducatorModel.builder().id(UUID.fromString(educatorId)).build();
    }

    @Named("levelRef")
    default LevelModel levelRef(String levelId) {
        if (levelId == null) {
            return null;
        }
        return LevelModel.builder().id(UUID.fromString(levelId)).build();
    }
}
