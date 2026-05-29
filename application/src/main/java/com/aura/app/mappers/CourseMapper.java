package com.aura.app.mappers;

import com.aura.app.codegen.types.CourseResponse;
import com.aura.app.codegen.types.CreateCourseInput;
import com.aura.app.codegen.types.CreatePriceRecordInput;
import com.aura.core.entities.courses.Course;
import com.aura.core.entities.lessons.PriceRecord;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {DateToEpochMapper.class, PriceRecordMapper.class})
public interface CourseMapper {
    CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "priceRecord", ignore = true)
    @Mapping(target = "outletId", source = "outletId")
    @Mapping(target = "levelId", source = "levelId")
    @Mapping(source = "institutionId", target = "tenantId")
    Course toDomainEntity(CreateCourseInput createCourseInput);

    @AfterMapping
    default void attachPriceRecord(CreateCourseInput input, @MappingTarget Course course) {
        if (input.getPriceRecord() != null) {
            course.setPriceRecord(mapPriceRecord(input.getPriceRecord(), input.getInstitutionId()));
        }
    }

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "courseId", ignore = true)
    PriceRecord mapPriceRecord(CreatePriceRecordInput createPriceRecordInput, String tenantId);

    CourseResponse toDto(Course course);
}
