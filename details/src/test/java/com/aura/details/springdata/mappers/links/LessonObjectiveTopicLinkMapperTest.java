package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.LessonObjectiveTopicLink;
import com.aura.details.springdata.models.lessons.LessonObjectiveModel;
import com.aura.details.springdata.models.links.LessonObjectiveTopicLinkModel;
import com.aura.details.springdata.models.topics.TopicModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("LessonObjectiveTopicLinkMapper")
class LessonObjectiveTopicLinkMapperTest {

    private final LessonObjectiveTopicLinkMapper mapper = LessonObjectiveTopicLinkMapper.INSTANCE;

    @DisplayName("Should map LessonObjectiveTopicLinkModel.topic.id into LessonObjectiveTopicLink.topicId without traversing back into Topic")
    @Test
    void toDomain_extractsTopicIdFromModel() {
        UUID topicId = UUID.randomUUID();
        TopicModel topicModel = TopicModel.builder().id(topicId).build();
        LessonObjectiveTopicLinkModel model = LessonObjectiveTopicLinkModel.builder()
                .id(UUID.randomUUID())
                .topic(topicModel)
                .build();

        LessonObjectiveTopicLink domain = mapper.toDomain(model);

        assertThat(domain.getTopicId()).isEqualTo(topicId.toString());
    }

    @DisplayName("Should map LessonObjectiveTopicLinkModel.lessonObjective.id into LessonObjectiveTopicLink.lessonObjectiveId without traversing back into LessonObjective")
    @Test
    void toDomain_extractsLessonObjectiveIdFromModel() {
        UUID lessonObjectiveId = UUID.randomUUID();
        LessonObjectiveModel lessonObjectiveModel = LessonObjectiveModel.builder().id(lessonObjectiveId).build();
        LessonObjectiveTopicLinkModel model = LessonObjectiveTopicLinkModel.builder()
                .id(UUID.randomUUID())
                .lessonObjective(lessonObjectiveModel)
                .build();

        LessonObjectiveTopicLink domain = mapper.toDomain(model);

        assertThat(domain.getLessonObjectiveId()).isEqualTo(lessonObjectiveId.toString());
    }

    @DisplayName("Should leave topic and lessonObjective null on toModelEntity — attached separately by the repository impl")
    @Test
    void toModelEntity_leavesRelationsNull() {
        LessonObjectiveTopicLink domain = LessonObjectiveTopicLink.builder()
                .id(UUID.randomUUID().toString())
                .topicId(UUID.randomUUID().toString())
                .lessonObjectiveId(UUID.randomUUID().toString())
                .build();

        LessonObjectiveTopicLinkModel model = mapper.toModelEntity(domain);

        assertThat(model.getTopic()).isNull();
        assertThat(model.getLessonObjective()).isNull();
    }
}
