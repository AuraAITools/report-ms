package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.TopicLessonLink;
import com.aura.details.springdata.models.lessons.LessonModel;
import com.aura.details.springdata.models.links.TopicLessonLinkModel;
import com.aura.details.springdata.models.topics.TopicModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TopicLessonLinkMapper")
class TopicLessonLinkMapperTest {

    private final TopicLessonLinkMapper mapper = TopicLessonLinkMapper.INSTANCE;

    @DisplayName("Should map TopicLessonLinkModel.topic.id into TopicLessonLink.topicId without traversing back into Topic")
    @Test
    void toDomain_extractsTopicIdFromModel() {
        UUID topicId = UUID.randomUUID();
        TopicModel topicModel = TopicModel.builder().id(topicId).build();
        TopicLessonLinkModel model = TopicLessonLinkModel.builder()
                .id(UUID.randomUUID())
                .topic(topicModel)
                .build();

        TopicLessonLink domain = mapper.toDomain(model);

        assertThat(domain.getTopicId()).isEqualTo(topicId.toString());
    }

    @DisplayName("Should map TopicLessonLinkModel.lesson.id into TopicLessonLink.lessonId without traversing back into Lesson")
    @Test
    void toDomain_extractsLessonIdFromModel() {
        UUID lessonId = UUID.randomUUID();
        LessonModel lessonModel = LessonModel.builder().id(lessonId).build();
        TopicLessonLinkModel model = TopicLessonLinkModel.builder()
                .id(UUID.randomUUID())
                .lesson(lessonModel)
                .build();

        TopicLessonLink domain = mapper.toDomain(model);

        assertThat(domain.getLessonId()).isEqualTo(lessonId.toString());
    }

    @DisplayName("Should leave TopicLessonLinkModel.topic null on toModelEntity — TopicLessonLinkRepositoryImpl attaches via Refs.ref")
    @Test
    void toModelEntity_leavesTopicNullForRepositoryImplToAttach() {
        TopicLessonLink domain = TopicLessonLink.builder()
                .id(UUID.randomUUID().toString())
                .topicId(UUID.randomUUID().toString())
                .build();

        TopicLessonLinkModel model = mapper.toModelEntity(domain);

        assertThat(model.getTopic()).isNull();
    }

    @DisplayName("Should leave TopicLessonLinkModel.lesson null on toModelEntity — TopicLessonLinkRepositoryImpl attaches via Refs.ref")
    @Test
    void toModelEntity_leavesLessonNullForRepositoryImplToAttach() {
        TopicLessonLink domain = TopicLessonLink.builder()
                .id(UUID.randomUUID().toString())
                .lessonId(UUID.randomUUID().toString())
                .build();

        TopicLessonLinkModel model = mapper.toModelEntity(domain);

        assertThat(model.getLesson()).isNull();
    }
}
