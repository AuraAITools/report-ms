package com.aura.details.springdata.mappers.lessons;

import com.aura.core.entities.lessons.LessonTopicProficiency;
import com.aura.details.springdata.models.lessons.LessonTopicProficiencyModel;
import com.aura.details.springdata.models.topics.TopicModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("LessonTopicProficiencyMapper")
class LessonTopicProficiencyMapperTest {

    private final LessonTopicProficiencyMapper mapper = LessonTopicProficiencyMapper.INSTANCE;

    @DisplayName("Should map LessonTopicProficiencyModel.topic.id into LessonTopicProficiency.topicId without traversing back into Topic")
    @Test
    void toDomainEntity_extractsTopicIdFromModel() {
        UUID topicId = UUID.randomUUID();
        TopicModel topicModel = TopicModel.builder()
                .id(topicId)
                .build();
        LessonTopicProficiencyModel model = LessonTopicProficiencyModel.builder()
                .id(UUID.randomUUID())
                .topic(topicModel)
                .rating(4)
                .build();

        LessonTopicProficiency domain = mapper.toDomainEntity(model);

        assertThat(domain.getTopicId()).isEqualTo(topicId.toString());
        assertThat(domain.getRating()).isEqualTo(4);
    }

    @DisplayName("Should set LessonTopicProficiencyModel.topic to a stub TopicModel populated only with the id from LessonTopicProficiency.topicId")
    @Test
    void toModelEntity_buildsTopicStubFromTopicId() {
        UUID topicId = UUID.randomUUID();
        LessonTopicProficiency domain = LessonTopicProficiency.builder()
                .id(UUID.randomUUID().toString())
                .topicId(topicId.toString())
                .rating(3)
                .build();

        LessonTopicProficiencyModel model = mapper.toModelEntity(domain);

        assertThat(model.getTopic()).isNotNull();
        assertThat(model.getTopic().getId()).isEqualTo(topicId);
        assertThat(model.getTopic().getName()).isNull();
        assertThat(model.getRating()).isEqualTo(3);
    }

    @DisplayName("Should leave LessonTopicProficiencyModel.topic null when LessonTopicProficiency.topicId is null")
    @Test
    void toModelEntity_returnsNullTopicWhenTopicIdNull() {
        LessonTopicProficiency domain = LessonTopicProficiency.builder()
                .id(UUID.randomUUID().toString())
                .topicId(null)
                .build();

        LessonTopicProficiencyModel model = mapper.toModelEntity(domain);

        assertThat(model.getTopic()).isNull();
    }

    @DisplayName("Should preserve topicId across a Model→Domain→Model round-trip")
    @Test
    void roundTrip_preservesTopicId() {
        UUID topicId = UUID.randomUUID();
        TopicModel topicModel = TopicModel.builder()
                .id(topicId)
                .build();
        LessonTopicProficiencyModel original = LessonTopicProficiencyModel.builder()
                .id(UUID.randomUUID())
                .topic(topicModel)
                .rating(5)
                .build();

        LessonTopicProficiencyModel roundTripped = mapper.toModelEntity(mapper.toDomainEntity(original));

        assertThat(roundTripped.getTopic().getId()).isEqualTo(topicId);
        assertThat(roundTripped.getRating()).isEqualTo(5);
    }
}
