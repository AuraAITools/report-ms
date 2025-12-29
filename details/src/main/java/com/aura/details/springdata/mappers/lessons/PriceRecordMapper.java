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

    @Mapping(target = "course.subjectCourseLinks", ignore = true)
    @Mapping(target = "course.studentCourseLinks", ignore = true)
    @Mapping(target = "course.educatorCourseLinks", ignore = true)
    @Mapping(target = "course.lessons", ignore = true)
    @Mapping(target = "course.level.subjectLevelLinks", ignore = true)
    @Mapping(target = "course.level.levelEducatorLinks", ignore = true)
    @Mapping(target = "course.level.courses", ignore = true)
    @Mapping(target = "course.level.students", ignore = true)
    PriceRecord toDomainEntity(PriceRecordTable priceRecordTable);

    @Mapping(target = "course.subjectCourseLinks", ignore = true)
    @Mapping(target = "course.studentCourseLinks", ignore = true)
    @Mapping(target = "course.educatorCourseLinks", ignore = true)
    @Mapping(target = "course.lessons", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "Id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    PriceRecordTable toTableEntity(PriceRecord priceRecord);
}

