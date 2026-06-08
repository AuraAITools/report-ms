package com.aura.app.mappers;

import com.aura.app.codegen.types.StudentLessonReportResponse;
import com.aura.core.entities.links.StudentLessonReport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {
        DateToEpochMapper.class,
        AccountMinimalDetailMapper.class,
        LessonHomeworkCompletionMapper.class,
        LessonParticipationReviewMapper.class,
        LessonTopicProficiencyMapper.class
})
public interface StudentLessonReportMapper {

    StudentLessonReportMapper INSTANCE = Mappers.getMapper(StudentLessonReportMapper.class);

    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "toMinimal")
    @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "toMinimal")
    StudentLessonReportResponse toDto(StudentLessonReport report);
}
