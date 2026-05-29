package com.aura.details.springdata.mappers.lessons;

import com.aura.core.entities.lessons.PriceRecord;
import com.aura.details.springdata.models.lessons.PriceRecordModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PriceRecordMapper {
    PriceRecordMapper INSTANCE = Mappers.getMapper(PriceRecordMapper.class);

    @Mapping(target = "courseId", ignore = true)
    PriceRecord toDomainEntity(PriceRecordModel model);

    // course is attached separately by PriceRecordRepositoryImpl via Refs.ref(entityManager, ...),
    // because a builder-stub CourseModel (id set, @Version null) trips Hibernate's
    // isTransient check ("Detached entity ... uninitialized version value 'null'") during persist.
    @Mapping(target = "course", ignore = true)
    PriceRecordModel toModelEntity(PriceRecord priceRecord);
}
