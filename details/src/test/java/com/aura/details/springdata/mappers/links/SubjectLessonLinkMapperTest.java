package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.SubjectLessonLink;
import com.aura.details.springdata.models.lessons.LessonModel;
import com.aura.details.springdata.models.links.SubjectLessonLinkModel;
import com.aura.details.springdata.models.subjects.SubjectModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SubjectLessonLinkMapper")
class SubjectLessonLinkMapperTest {

    private final SubjectLessonLinkMapper mapper = SubjectLessonLinkMapper.INSTANCE;

    @DisplayName("Should map SubjectLessonLinkModel.subject.id into SubjectLessonLink.subjectId without traversing back into Subject")
    @Test
    void toDomain_extractsSubjectIdFromModel() {
        UUID subjectId = UUID.randomUUID();
        SubjectModel subjectModel = SubjectModel.builder().id(subjectId).build();
        SubjectLessonLinkModel model = SubjectLessonLinkModel.builder()
                .id(UUID.randomUUID())
                .subject(subjectModel)
                .build();

        SubjectLessonLink domain = mapper.toDomain(model);

        assertThat(domain.getSubjectId()).isEqualTo(subjectId.toString());
    }

    @DisplayName("Should map SubjectLessonLinkModel.lesson.id into SubjectLessonLink.lessonId without traversing back into Lesson")
    @Test
    void toDomain_extractsLessonIdFromModel() {
        UUID lessonId = UUID.randomUUID();
        LessonModel lessonModel = LessonModel.builder().id(lessonId).build();
        SubjectLessonLinkModel model = SubjectLessonLinkModel.builder()
                .id(UUID.randomUUID())
                .lesson(lessonModel)
                .build();

        SubjectLessonLink domain = mapper.toDomain(model);

        assertThat(domain.getLessonId()).isEqualTo(lessonId.toString());
    }

    @DisplayName("Should leave SubjectLessonLinkModel.subject null on toModelEntity — SubjectLessonLinkRepositoryImpl attaches via Refs.ref")
    @Test
    void toModelEntity_leavesSubjectNullForRepositoryImplToAttach() {
        SubjectLessonLink domain = SubjectLessonLink.builder()
                .id(UUID.randomUUID().toString())
                .subjectId(UUID.randomUUID().toString())
                .build();

        SubjectLessonLinkModel model = mapper.toModelEntity(domain);

        assertThat(model.getSubject()).isNull();
    }

    @DisplayName("Should leave SubjectLessonLinkModel.lesson null on toModelEntity — SubjectLessonLinkRepositoryImpl attaches via Refs.ref")
    @Test
    void toModelEntity_leavesLessonNullForRepositoryImplToAttach() {
        SubjectLessonLink domain = SubjectLessonLink.builder()
                .id(UUID.randomUUID().toString())
                .lessonId(UUID.randomUUID().toString())
                .build();

        SubjectLessonLinkModel model = mapper.toModelEntity(domain);

        assertThat(model.getLesson()).isNull();
    }
}
