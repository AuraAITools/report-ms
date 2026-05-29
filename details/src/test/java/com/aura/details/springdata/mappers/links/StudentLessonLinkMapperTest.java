package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.StudentLessonLink;
import com.aura.details.springdata.models.lessons.LessonModel;
import com.aura.details.springdata.models.links.StudentLessonLinkModel;
import com.aura.details.springdata.models.students.StudentModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("StudentLessonLinkMapper")
class StudentLessonLinkMapperTest {

    private final StudentLessonLinkMapper mapper = StudentLessonLinkMapper.INSTANCE;

    @DisplayName("Should map StudentLessonLinkModel.student.id into StudentLessonLink.studentId without traversing back into Student")
    @Test
    void toDomainEntity_extractsStudentIdFromModel() {
        UUID studentId = UUID.randomUUID();
        StudentModel studentModel = StudentModel.builder().id(studentId).build();
        StudentLessonLinkModel model = StudentLessonLinkModel.builder()
                .id(UUID.randomUUID())
                .student(studentModel)
                .build();

        StudentLessonLink domain = mapper.toDomainEntity(model);

        assertThat(domain.getStudentId()).isEqualTo(studentId.toString());
    }

    @DisplayName("Should map StudentLessonLinkModel.lesson.id into StudentLessonLink.lessonId without traversing back into Lesson")
    @Test
    void toDomainEntity_extractsLessonIdFromModel() {
        UUID lessonId = UUID.randomUUID();
        LessonModel lessonModel = LessonModel.builder().id(lessonId).build();
        StudentLessonLinkModel model = StudentLessonLinkModel.builder()
                .id(UUID.randomUUID())
                .lesson(lessonModel)
                .build();

        StudentLessonLink domain = mapper.toDomainEntity(model);

        assertThat(domain.getLessonId()).isEqualTo(lessonId.toString());
    }

    @DisplayName("Should leave StudentLessonLinkModel.student null on toModelEntity — StudentLessonLinkRepositoryImpl attaches via Refs.ref")
    @Test
    void toModelEntity_leavesStudentNullForRepositoryImplToAttach() {
        StudentLessonLink domain = StudentLessonLink.builder()
                .id(UUID.randomUUID().toString())
                .studentId(UUID.randomUUID().toString())
                .build();

        StudentLessonLinkModel model = mapper.toModelEntity(domain);

        assertThat(model.getStudent()).isNull();
    }

    @DisplayName("Should leave StudentLessonLinkModel.lesson null on toModelEntity — StudentLessonLinkRepositoryImpl attaches via Refs.ref")
    @Test
    void toModelEntity_leavesLessonNullForRepositoryImplToAttach() {
        StudentLessonLink domain = StudentLessonLink.builder()
                .id(UUID.randomUUID().toString())
                .lessonId(UUID.randomUUID().toString())
                .build();

        StudentLessonLinkModel model = mapper.toModelEntity(domain);

        assertThat(model.getLesson()).isNull();
    }
}
