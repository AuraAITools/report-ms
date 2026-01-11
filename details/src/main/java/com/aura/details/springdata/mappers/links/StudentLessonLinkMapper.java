package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.StudentLessonLink;
import com.aura.details.springdata.mappers.lessons.LessonMapper;
import com.aura.details.springdata.mappers.students.StudentMapper;
import com.aura.details.springdata.tables.links.StudentLessonLinkTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {StudentMapper.class, LessonMapper.class})
public interface StudentLessonLinkMapper {
    StudentLessonLinkMapper INSTANCE = Mappers.getMapper(StudentLessonLinkMapper.class);

    @Mapping(target = "student", ignore = true)
    @Mapping(target = "lesson", ignore = true)
    @Mapping(target = "lessonPlan", ignore = true)
    @Mapping(target = "lessonHomeworkCompletion", ignore = true)
    @Mapping(target = "studentLessonAttendance", ignore = true)
    @Mapping(target = "lessonParticipationReview", ignore = true)
    @Mapping(target = "lessonQuizStudentLessonLinks", ignore = true)
    @Mapping(target = "lessonTopicProficiency", ignore = true)
    StudentLessonLink toDomainEntity(StudentLessonLinkTable studentLessonLinkTable);

    @Mapping(target = "student", ignore = true)
    @Mapping(target = "lesson", ignore = true)
    @Mapping(target = "lessonPlan", ignore = true)
    @Mapping(target = "lessonHomeworkCompletion", ignore = true)
    @Mapping(target = "studentLessonAttendance", ignore = true)
    @Mapping(target = "lessonParticipationReview", ignore = true)
    @Mapping(target = "lessonQuizStudentLessonLinks", ignore = true)
    @Mapping(target = "lessonTopicProficiency", ignore = true)
    @Mapping(target = "Id", source = "id")
    StudentLessonLinkTable toTableEntity(StudentLessonLink studentLessonLink);
}

