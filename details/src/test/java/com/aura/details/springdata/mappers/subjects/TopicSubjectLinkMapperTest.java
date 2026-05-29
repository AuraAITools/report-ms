package com.aura.details.springdata.mappers.subjects;

import com.aura.core.entities.links.TopicSubjectLink;
import com.aura.details.springdata.models.links.TopicSubjectLinkModel;
import com.aura.details.springdata.models.subjects.SubjectModel;
import com.aura.details.springdata.models.topics.TopicModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TopicSubjectLinkMapper")
class TopicSubjectLinkMapperTest {

    private final TopicSubjectLinkMapper mapper = TopicSubjectLinkMapper.INSTANCE;

    @DisplayName("Should map TopicSubjectLinkModel.topic.id into TopicSubjectLink.topicId without traversing back into Topic")
    @Test
    void toDomain_extractsTopicIdFromModel() {
        UUID topicId = UUID.randomUUID();
        TopicModel topicModel = TopicModel.builder().id(topicId).build();
        TopicSubjectLinkModel model = TopicSubjectLinkModel.builder()
                .id(UUID.randomUUID())
                .topic(topicModel)
                .build();

        TopicSubjectLink domain = mapper.toDomain(model);

        assertThat(domain.getTopicId()).isEqualTo(topicId.toString());
    }

    @DisplayName("Should map TopicSubjectLinkModel.subject.id into TopicSubjectLink.subjectId without traversing back into Subject")
    @Test
    void toDomain_extractsSubjectIdFromModel() {
        UUID subjectId = UUID.randomUUID();
        SubjectModel subjectModel = SubjectModel.builder().id(subjectId).build();
        TopicSubjectLinkModel model = TopicSubjectLinkModel.builder()
                .id(UUID.randomUUID())
                .subject(subjectModel)
                .build();

        TopicSubjectLink domain = mapper.toDomain(model);

        assertThat(domain.getSubjectId()).isEqualTo(subjectId.toString());
    }

    @DisplayName("Should set TopicSubjectLinkModel.topic to a stub TopicModel populated only with the id from TopicSubjectLink.topicId")
    @Test
    void toTable_buildsTopicStubFromTopicId() {
        UUID topicId = UUID.randomUUID();
        TopicSubjectLink domain = TopicSubjectLink.builder()
                .id(UUID.randomUUID().toString())
                .topicId(topicId.toString())
                .build();

        TopicSubjectLinkModel model = mapper.toModelEntity(domain);

        assertThat(model.getTopic()).isNotNull();
        assertThat(model.getTopic().getId()).isEqualTo(topicId);
        assertThat(model.getTopic().getName()).isNull();
    }

    @DisplayName("Should set TopicSubjectLinkModel.subject to a stub SubjectModel populated only with the id from TopicSubjectLink.subjectId")
    @Test
    void toTable_buildsSubjectStubFromSubjectId() {
        UUID subjectId = UUID.randomUUID();
        TopicSubjectLink domain = TopicSubjectLink.builder()
                .id(UUID.randomUUID().toString())
                .subjectId(subjectId.toString())
                .build();

        TopicSubjectLinkModel model = mapper.toModelEntity(domain);

        assertThat(model.getSubject()).isNotNull();
        assertThat(model.getSubject().getId()).isEqualTo(subjectId);
        assertThat(model.getSubject().getName()).isNull();
    }

    @DisplayName("Should leave TopicSubjectLinkModel.topic null when TopicSubjectLink.topicId is null")
    @Test
    void toTable_returnsNullTopicWhenTopicIdNull() {
        TopicSubjectLink domain = TopicSubjectLink.builder()
                .id(UUID.randomUUID().toString())
                .topicId(null)
                .build();

        TopicSubjectLinkModel model = mapper.toModelEntity(domain);

        assertThat(model.getTopic()).isNull();
    }

    @DisplayName("Should leave TopicSubjectLinkModel.subject null when TopicSubjectLink.subjectId is null")
    @Test
    void toTable_returnsNullSubjectWhenSubjectIdNull() {
        TopicSubjectLink domain = TopicSubjectLink.builder()
                .id(UUID.randomUUID().toString())
                .subjectId(null)
                .build();

        TopicSubjectLinkModel model = mapper.toModelEntity(domain);

        assertThat(model.getSubject()).isNull();
    }

    @DisplayName("Should preserve topicId and subjectId across a Model→Domain→Model round-trip")
    @Test
    void roundTrip_preservesTopicIdAndSubjectId() {
        UUID topicId = UUID.randomUUID();
        UUID subjectId = UUID.randomUUID();
        TopicModel topicModel = TopicModel.builder().id(topicId).build();
        SubjectModel subjectModel = SubjectModel.builder().id(subjectId).build();
        TopicSubjectLinkModel original = TopicSubjectLinkModel.builder()
                .id(UUID.randomUUID())
                .topic(topicModel)
                .subject(subjectModel)
                .build();

        TopicSubjectLinkModel roundTripped = mapper.toModelEntity(mapper.toDomain(original));

        assertThat(roundTripped.getTopic().getId()).isEqualTo(topicId);
        assertThat(roundTripped.getSubject().getId()).isEqualTo(subjectId);
    }
}
