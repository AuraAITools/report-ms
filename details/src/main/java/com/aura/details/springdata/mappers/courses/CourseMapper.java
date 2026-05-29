package com.aura.details.springdata.mappers.courses;

import com.aura.core.entities.courses.Course;
import com.aura.details.springdata.mappers.lessons.PriceRecordMapper;
import com.aura.details.springdata.models.courses.CourseModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {PriceRecordMapper.class})
public interface CourseMapper {
    CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);

    @Mapping(source = "outlet.id", target = "outletId")
    @Mapping(source = "level.id", target = "levelId")
    Course toDomainEntity(CourseModel courseModel);

    // outlet and level are attached separately by CourseRepositoryImpl via Refs.ref(entityManager, ...),
    // because a builder-stub *Model (id set, @Version null) trips Hibernate's
    // isTransient check ("Detached entity ... uninitialized version value 'null'") during persist.
    @Mapping(target = "outlet", ignore = true)
    @Mapping(target = "level", ignore = true)
    CourseModel toModelEntity(Course course);
}
