package com.aura.details.springdata.mappers.courses;

import com.aura.core.entities.courses.Course;
import com.aura.details.springdata.mappers.lessons.PriceRecordMapper;
import com.aura.details.springdata.mappers.levels.LevelMapper;
import com.aura.details.springdata.mappers.outlets.OutletMapper;
import com.aura.details.springdata.tables.courses.CourseTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {PriceRecordMapper.class, LevelMapper.class, OutletMapper.class})
public interface CourseMapper {
    CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);

    @Mapping(target = "subjectCourseLinks", ignore = true)
    @Mapping(target = "studentCourseLinks", ignore = true)
    @Mapping(target = "educatorCourseLinks", ignore = true)
    @Mapping(target = "lessons", ignore = true)
    @Mapping(target = "level.subjectLevelLinks", ignore = true)
    @Mapping(target = "level.levelEducatorLinks", ignore = true)
    @Mapping(target = "level.courses", ignore = true)
    @Mapping(target = "level.students", ignore = true)
    @Mapping(target = "priceRecord", ignore = true)
    Course toDomainEntity(CourseTable courseTable);

    @Mapping(target = "subjectCourseLinks", ignore = true)
    @Mapping(target = "studentCourseLinks", ignore = true)
    @Mapping(target = "educatorCourseLinks", ignore = true)
    @Mapping(target = "lessons", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "Id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    CourseTable toTableEntity(Course course);
}

