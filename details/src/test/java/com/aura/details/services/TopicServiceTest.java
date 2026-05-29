package com.aura.details.services;

import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.entities.topics.Topic;
import com.aura.core.repositories.TopicRepository;
import com.aura.core.usecases.topics.CreateTopicUseCase;
import com.aura.core.usecases.topics.DeleteTopicUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Topic service")
@ExtendWith(MockitoExtension.class)
class TopicServiceTest {

    @Mock
    private CreateTopicUseCase createTopicUseCase;
    @Mock
    private DeleteTopicUseCase deleteTopicUseCase;
    @Mock
    private TopicRepository topicRepository;
    @Mock
    private RelationshipAuthorizationService relationshipService;

    @InjectMocks
    private TopicService topicService;

    @DisplayName("Should create topic and write parent relationship to the institution")
    @Test
    void createTopic_writesParentInstitutionRelationship() {
        Topic input = Topic.builder().name("Algebra").tenantId("tenant-1").build();
        Topic created = Topic.builder().id("topic-1").name("Algebra").tenantId("tenant-1").build();
        when(createTopicUseCase.createTopic(input)).thenReturn(created);

        Topic result = topicService.createTopic(input);

        assertThat(result).isEqualTo(created);
        verify(relationshipService).writeRelationship(
                "topic", "topic-1", "parent", "institution", "tenant-1");
    }

    @DisplayName("Should delete topic and wipe spicedb relationships in order")
    @Test
    void deleteById_clearsLinksAndSpicedb() {
        topicService.deleteById("topic-1");

        InOrder ordered = inOrder(topicRepository, deleteTopicUseCase, relationshipService);
        ordered.verify(topicRepository).deleteAllTopicSubjectLinksByTopicId("topic-1");
        ordered.verify(deleteTopicUseCase).deleteById("topic-1");
        ordered.verify(relationshipService).deleteAllRelationshipsForResource("topic", "topic-1");
        ordered.verify(relationshipService).deleteAllRelationshipsForResource("topic_user_group", "topic-1");
    }
}
