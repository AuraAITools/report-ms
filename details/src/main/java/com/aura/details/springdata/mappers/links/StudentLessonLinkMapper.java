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

    @Mapping(target = "lessonPlan", ignore = true)
    @Mapping(target = "lessonHomeworkCompletion", ignore = true)
    @Mapping(target = "studentLessonAttendance", ignore = true)
    @Mapping(target = "lessonParticipationReview", ignore = true)
    @Mapping(target = "lessonQuizStudentLessonLinks", ignore = true)
    @Mapping(target = "lessonTopicProficiency", ignore = true)
    @Mapping(target = "student.subjectStudentLinks", ignore = true)
    @Mapping(target = "student.testResults", ignore = true)
    @Mapping(target = "student.studentLessonLinks", ignore = true)
    @Mapping(target = "student.accountStudentLinks", ignore = true)
    @Mapping(target = "student.studentCourseLinks", ignore = true)
    @Mapping(target = "student.studentOutletLinks", ignore = true)
    @Mapping(target = "student.level.subjectLevelLinks", ignore = true)
    @Mapping(target = "student.level.levelEducatorLinks", ignore = true)
    @Mapping(target = "student.level.courses", ignore = true)
    @Mapping(target = "student.level.students", ignore = true)
    @Mapping(target = "student.school.students", ignore = true)
    @Mapping(target = "lesson.lessonPostponementRequestTables", ignore = true)
    @Mapping(target = "lesson.lessonCancellationRequests", ignore = true)
    @Mapping(target = "lesson.lessonQuizzTables", ignore = true)
    @Mapping(target = "lesson.subject.subjectCourseLinks", ignore = true)
    @Mapping(target = "lesson.subject.lessons", ignore = true)
    @Mapping(target = "lesson.subject.subjectStudentLinks", ignore = true)
    @Mapping(target = "lesson.subject.subjectEducatorLinks", ignore = true)
    @Mapping(target = "lesson.subject.subjectTestGroupLinks", ignore = true)
    @Mapping(target = "lesson.subject.testResults", ignore = true)
    @Mapping(target = "lesson.subject.subjectLevelLinks", ignore = true)
    @Mapping(target = "lesson.subject.topicSubjectLinks", ignore = true)
    StudentLessonLink toDomainEntity(StudentLessonLinkTable studentLessonLinkTable);

    @Mapping(target = "lessonPlan", ignore = true)
    @Mapping(target = "lessonHomeworkCompletion", ignore = true)
    @Mapping(target = "studentLessonAttendance", ignore = true)
    @Mapping(target = "lessonParticipationReview", ignore = true)
    @Mapping(target = "lessonQuizStudentLessonLinks", ignore = true)
    @Mapping(target = "lessonTopicProficiency", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "Id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    StudentLessonLinkTable toTableEntity(StudentLessonLink studentLessonLink);
}

