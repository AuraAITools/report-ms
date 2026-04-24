package com.aura.app.mappers;

import com.aura.app.codegen.types.CourseResponse;
import com.aura.app.codegen.types.CreateCourseInput;
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
    @Mapping(target = "subjectCourseLinks", ignore = true)
    @Mapping(target = "studentCourseLinks", ignore = true)
    @Mapping(target = "level", ignore = true)
    @Mapping(target = "lessons", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "educatorCourseLinks", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "priceRecord", ignore = true)
    @Mapping(target = "outlet.id", source = "outletId")
    @Mapping(source = "institutionId", target = "tenantId")
    Course toDomainEntity(CreateCourseInput createCourseInput);

    @AfterMapping
    default void mapPriceRecord(CreateCourseInput createCourseInput,
                                @MappingTarget Course course) {
        if (createCourseInput.getPriceRecord() != null) {
            PriceRecord priceRecord = PriceRecordMapper.INSTANCE
                    .toDomainEntity(createCourseInput.getPriceRecord(), createCourseInput.getInstitutionId());
            course.setPriceRecord(priceRecord);
        }
    }

    CourseResponse toDto(Course course);
}
