package com.aura.details.springdata.mappers.lessons;

import com.aura.core.entities.links.EducatorLessonLink;
import com.aura.details.springdata.models.educators.EducatorModel;
import com.aura.details.springdata.models.lessons.LessonModel;
import com.aura.details.springdata.models.links.EducatorLessonLinkModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("EducatorLessonLinkMapper")
class EducatorLessonLinkMapperTest {

    private final EducatorLessonLinkMapper mapper = EducatorLessonLinkMapper.INSTANCE;

    @DisplayName("Should map EducatorLessonLinkModel.educator.id into EducatorLessonLink.educatorId without traversing back into Educator")
    @Test
    void toDomain_extractsEducatorIdFromModel() {
        UUID educatorId = UUID.randomUUID();
        EducatorModel educatorModel = EducatorModel.builder().id(educatorId).build();
        EducatorLessonLinkModel model = EducatorLessonLinkModel.builder()
                .id(UUID.randomUUID())
                .educator(educatorModel)
                .build();

        EducatorLessonLink domain = mapper.toDomain(model);

        assertThat(domain.getEducatorId()).isEqualTo(educatorId.toString());
    }

    @DisplayName("Should map EducatorLessonLinkModel.lesson.id into EducatorLessonLink.lessonId without traversing back into Lesson")
    @Test
    void toDomain_extractsLessonIdFromModel() {
        UUID lessonId = UUID.randomUUID();
        LessonModel lessonModel = LessonModel.builder().id(lessonId).build();
        EducatorLessonLinkModel model = EducatorLessonLinkModel.builder()
                .id(UUID.randomUUID())
                .lesson(lessonModel)
                .build();

        EducatorLessonLink domain = mapper.toDomain(model);

        assertThat(domain.getLessonId()).isEqualTo(lessonId.toString());
    }

    @DisplayName("Should leave EducatorLessonLinkModel.educator null on toModelEntity — EducatorLessonLinkRepositoryImpl attaches via Refs.ref")
    @Test
    void toModelEntity_leavesEducatorNullForRepositoryImplToAttach() {
        EducatorLessonLink domain = EducatorLessonLink.builder()
                .id(UUID.randomUUID().toString())
                .educatorId(UUID.randomUUID().toString())
                .build();

        EducatorLessonLinkModel model = mapper.toModelEntity(domain);

        assertThat(model.getEducator()).isNull();
    }

    @DisplayName("Should leave EducatorLessonLinkModel.lesson null on toModelEntity — EducatorLessonLinkRepositoryImpl attaches via Refs.ref")
    @Test
    void toModelEntity_leavesLessonNullForRepositoryImplToAttach() {
        EducatorLessonLink domain = EducatorLessonLink.builder()
                .id(UUID.randomUUID().toString())
                .lessonId(UUID.randomUUID().toString())
                .build();

        EducatorLessonLinkModel model = mapper.toModelEntity(domain);

        assertThat(model.getLesson()).isNull();
    }
}
