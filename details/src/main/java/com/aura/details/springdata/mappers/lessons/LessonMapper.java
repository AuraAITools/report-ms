package com.aura.details.springdata.mappers.lessons;

import com.aura.core.entities.lessons.Lesson;
import com.aura.details.springdata.models.lessons.LessonModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {LessonOutletRoomBookingMapper.class})
public interface LessonMapper {
    LessonMapper INSTANCE = Mappers.getMapper(LessonMapper.class);

    @Mapping(source = "course.id", target = "courseId")
    @Mapping(source = "outlet.id", target = "outletId")
    Lesson toDomainEntity(LessonModel lessonModel);

    // course and outlet are attached separately by LessonRepositoryImpl via Refs.ref(entityManager, ...),
    // because a builder-stub *Model (id set, @Version null) trips Hibernate's
    // isTransient check ("Detached entity ... uninitialized version value 'null'") during persist.
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "outlet", ignore = true)
    LessonModel toModelEntity(Lesson lesson);
}
