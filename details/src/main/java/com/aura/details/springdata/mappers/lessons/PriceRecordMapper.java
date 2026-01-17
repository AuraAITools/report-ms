package com.aura.details.springdata.mappers.lessons;

import com.aura.core.entities.lessons.PriceRecord;
import com.aura.details.springdata.mappers.courses.CourseMapper;
import com.aura.details.springdata.tables.lessons.PriceRecordTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {CourseMapper.class})
public interface PriceRecordMapper {
    PriceRecordMapper INSTANCE = Mappers.getMapper(PriceRecordMapper.class);

    @Mapping(target = "course", ignore = true)
    PriceRecord toDomainEntity(PriceRecordTable priceRecordTable);

    @Mapping(target = "course", ignore = true)
    @Mapping(target = "Id", source = "id")
    PriceRecordTable toTableEntity(PriceRecord priceRecord);
}

