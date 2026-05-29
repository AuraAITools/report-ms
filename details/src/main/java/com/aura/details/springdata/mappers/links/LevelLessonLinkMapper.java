package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.LevelLessonLink;
import com.aura.details.springdata.models.links.LevelLessonLinkModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LevelLessonLinkMapper {
    LevelLessonLinkMapper INSTANCE = Mappers.getMapper(LevelLessonLinkMapper.class);

    // level and lesson are attached separately by LevelLessonLinkRepositoryImpl via Refs.ref(entityManager, ...),
    // because a builder-stub *Model (id set, @Version null) trips Hibernate's
    // isTransient check ("Detached entity ... uninitialized version value 'null'") during persist.
    @Mapping(target = "level", ignore = true)
    @Mapping(target = "lesson", ignore = true)
    LevelLessonLinkModel toModelEntity(LevelLessonLink domain);

    @Mapping(source = "level.id", target = "levelId")
    @Mapping(source = "lesson.id", target = "lessonId")
    LevelLessonLink toDomain(LevelLessonLinkModel table);
}
