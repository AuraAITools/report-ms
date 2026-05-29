package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.LessonPlanTopicLink;
import com.aura.details.springdata.models.lessons.LessonPlanModel;
import com.aura.details.springdata.models.links.LessonPlanTopicLinkModel;
import com.aura.details.springdata.models.topics.TopicModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("LessonPlanTopicLinkMapper")
class LessonPlanTopicLinkMapperTest {

    private final LessonPlanTopicLinkMapper mapper = LessonPlanTopicLinkMapper.INSTANCE;

    @DisplayName("Should map LessonPlanTopicLinkModel.lessonPlan.id into LessonPlanTopicLink.lessonPlanId without traversing back into LessonPlan")
    @Test
    void toDomain_extractsLessonPlanIdFromModel() {
        UUID lessonPlanId = UUID.randomUUID();
        LessonPlanModel lessonPlanModel = LessonPlanModel.builder().id(lessonPlanId).build();
        LessonPlanTopicLinkModel model = LessonPlanTopicLinkModel.builder()
                .id(UUID.randomUUID())
                .lessonPlan(lessonPlanModel)
                .build();

        LessonPlanTopicLink domain = mapper.toDomain(model);

        assertThat(domain.getLessonPlanId()).isEqualTo(lessonPlanId.toString());
    }

    @DisplayName("Should map LessonPlanTopicLinkModel.topic.id into LessonPlanTopicLink.topicId without traversing back into Topic")
    @Test
    void toDomain_extractsTopicIdFromModel() {
        UUID topicId = UUID.randomUUID();
        TopicModel topicModel = TopicModel.builder().id(topicId).build();
        LessonPlanTopicLinkModel model = LessonPlanTopicLinkModel.builder()
                .id(UUID.randomUUID())
                .topic(topicModel)
                .build();

        LessonPlanTopicLink domain = mapper.toDomain(model);

        assertThat(domain.getTopicId()).isEqualTo(topicId.toString());
    }

    @DisplayName("Should leave LessonPlanTopicLinkModel.lessonPlan null because lessonPlan is attached by repository impl via EntityManager.getReference, not by the mapper")
    @Test
    void toTable_doesNotBuildLessonPlanStub() {
        UUID lessonPlanId = UUID.randomUUID();
        LessonPlanTopicLink domain = LessonPlanTopicLink.builder()
                .id(UUID.randomUUID().toString())
                .lessonPlanId(lessonPlanId.toString())
                .build();

        LessonPlanTopicLinkModel model = mapper.toTable(domain);

        assertThat(model.getLessonPlan()).isNull();
    }

    @DisplayName("Should leave LessonPlanTopicLinkModel.topic null because topic is attached by repository impl via EntityManager.getReference, not by the mapper")
    @Test
    void toTable_doesNotBuildTopicStub() {
        UUID topicId = UUID.randomUUID();
        LessonPlanTopicLink domain = LessonPlanTopicLink.builder()
                .id(UUID.randomUUID().toString())
                .topicId(topicId.toString())
                .build();

        LessonPlanTopicLinkModel model = mapper.toTable(domain);

        assertThat(model.getTopic()).isNull();
    }

    @DisplayName("Should preserve link id across a Model→Domain→Model round-trip (lessonPlan and topic are not roundtripped through the mapper)")
    @Test
    void roundTrip_preservesLinkId() {
        UUID lessonPlanId = UUID.randomUUID();
        UUID topicId = UUID.randomUUID();
        UUID linkId = UUID.randomUUID();
        LessonPlanModel lessonPlanModel = LessonPlanModel.builder().id(lessonPlanId).build();
        TopicModel topicModel = TopicModel.builder().id(topicId).build();
        LessonPlanTopicLinkModel original = LessonPlanTopicLinkModel.builder()
                .id(linkId)
                .lessonPlan(lessonPlanModel)
                .topic(topicModel)
                .build();

        LessonPlanTopicLinkModel roundTripped = mapper.toTable(mapper.toDomain(original));

        assertThat(roundTripped.getLessonPlan()).isNull();
        assertThat(roundTripped.getTopic()).isNull();
        assertThat(roundTripped.getId()).isEqualTo(linkId);
    }
}
