package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.LessonGroupPlanTopicLink;
import com.aura.details.springdata.models.lessons.LessonGroupPlanModel;
import com.aura.details.springdata.models.links.LessonGroupPlanTopicLinkModel;
import com.aura.details.springdata.models.topics.TopicModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("LessonGroupPlanTopicLinkMapper")
class LessonPlanTopicLinkMapperTest {

    private final LessonGroupPlanTopicLinkMapper mapper = LessonGroupPlanTopicLinkMapper.INSTANCE;

    @DisplayName("Should map LessonGroupPlanTopicLinkModel.lessonGroupPlan.id into LessonGroupPlanTopicLink.lessonGroupPlanId without traversing back into LessonGroupPlan")
    @Test
    void toDomain_extractsLessonGroupPlanIdFromModel() {
        UUID lessonGroupPlanId = UUID.randomUUID();
        LessonGroupPlanModel lessonGroupPlanModel = LessonGroupPlanModel.builder().id(lessonGroupPlanId).build();
        LessonGroupPlanTopicLinkModel model = LessonGroupPlanTopicLinkModel.builder()
                .id(UUID.randomUUID())
                .lessonGroupPlan(lessonGroupPlanModel)
                .build();

        LessonGroupPlanTopicLink domain = mapper.toDomain(model);

        assertThat(domain.getLessonGroupPlanId()).isEqualTo(lessonGroupPlanId.toString());
    }

    @DisplayName("Should map LessonGroupPlanTopicLinkModel.topic.id into LessonGroupPlanTopicLink.topicId without traversing back into Topic")
    @Test
    void toDomain_extractsTopicIdFromModel() {
        UUID topicId = UUID.randomUUID();
        TopicModel topicModel = TopicModel.builder().id(topicId).build();
        LessonGroupPlanTopicLinkModel model = LessonGroupPlanTopicLinkModel.builder()
                .id(UUID.randomUUID())
                .topic(topicModel)
                .build();

        LessonGroupPlanTopicLink domain = mapper.toDomain(model);

        assertThat(domain.getTopicId()).isEqualTo(topicId.toString());
    }

    @DisplayName("Should leave LessonGroupPlanTopicLinkModel.lessonGroupPlan null because lessonGroupPlan is attached by repository impl via EntityManager.getReference, not by the mapper")
    @Test
    void toTable_doesNotBuildLessonGroupPlanStub() {
        UUID lessonGroupPlanId = UUID.randomUUID();
        LessonGroupPlanTopicLink domain = LessonGroupPlanTopicLink.builder()
                .id(UUID.randomUUID().toString())
                .lessonGroupPlanId(lessonGroupPlanId.toString())
                .build();

        LessonGroupPlanTopicLinkModel model = mapper.toTable(domain);

        assertThat(model.getLessonGroupPlan()).isNull();
    }

    @DisplayName("Should leave LessonGroupPlanTopicLinkModel.topic null because topic is attached by repository impl via EntityManager.getReference, not by the mapper")
    @Test
    void toTable_doesNotBuildTopicStub() {
        UUID topicId = UUID.randomUUID();
        LessonGroupPlanTopicLink domain = LessonGroupPlanTopicLink.builder()
                .id(UUID.randomUUID().toString())
                .topicId(topicId.toString())
                .build();

        LessonGroupPlanTopicLinkModel model = mapper.toTable(domain);

        assertThat(model.getTopic()).isNull();
    }

    @DisplayName("Should preserve link id across a Model→Domain→Model round-trip (lessonGroupPlan and topic are not roundtripped through the mapper)")
    @Test
    void roundTrip_preservesLinkId() {
        UUID lessonGroupPlanId = UUID.randomUUID();
        UUID topicId = UUID.randomUUID();
        UUID linkId = UUID.randomUUID();
        LessonGroupPlanModel lessonGroupPlanModel = LessonGroupPlanModel.builder().id(lessonGroupPlanId).build();
        TopicModel topicModel = TopicModel.builder().id(topicId).build();
        LessonGroupPlanTopicLinkModel original = LessonGroupPlanTopicLinkModel.builder()
                .id(linkId)
                .lessonGroupPlan(lessonGroupPlanModel)
                .topic(topicModel)
                .build();

        LessonGroupPlanTopicLinkModel roundTripped = mapper.toTable(mapper.toDomain(original));

        assertThat(roundTripped.getLessonGroupPlan()).isNull();
        assertThat(roundTripped.getTopic()).isNull();
        assertThat(roundTripped.getId()).isEqualTo(linkId);
    }
}
