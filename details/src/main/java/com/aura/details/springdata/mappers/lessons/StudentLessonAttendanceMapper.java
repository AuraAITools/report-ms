package com.aura.details.springdata.mappers.lessons;

import com.aura.core.entities.lessons.StudentLessonAttendance;
import com.aura.details.springdata.mappers.links.StudentLessonLinkMapper;
import com.aura.details.springdata.tables.lessons.StudentLessonAttendanceTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {StudentLessonLinkMapper.class})
public interface StudentLessonAttendanceMapper {
    StudentLessonAttendanceMapper INSTANCE = Mappers.getMapper(StudentLessonAttendanceMapper.class);

    StudentLessonAttendance toDomainEntity(StudentLessonAttendanceTable studentLessonAttendanceTable);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "Id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    StudentLessonAttendanceTable toTableEntity(StudentLessonAttendance studentLessonAttendance);
}

