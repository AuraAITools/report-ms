package com.aura.details.services;

import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.entities.delete_impact.AffectedResourceGroup;
import com.aura.core.entities.delete_impact.CleanupAction;
import com.aura.core.entities.delete_impact.DeleteImpact;
import com.aura.core.entities.topics.Topic;
import com.aura.core.repositories.TopicRepository;
import com.aura.core.usecases.topics.CreateTopicUseCase;
import com.aura.core.usecases.topics.DeleteTopicUseCase;
import com.aura.core.usecases.topics.GetTopicDeleteImpactUseCase;
import com.aura.core.usecases.topics.TopicQueryUseCase;
import java.util.List;
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
    private TopicQueryUseCase topicQueryUseCase;
    @Mock
    private DeleteTopicUseCase deleteTopicUseCase;
    @Mock
    private GetTopicDeleteImpactUseCase getTopicDeleteImpactUseCase;
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

    @DisplayName("Should delete topic, purge all 5 link tables, then wipe spicedb relationships in order")
    @Test
    void deleteById_clearsAllLinksAndSpicedb() {
        topicService.deleteById("topic-1");

        InOrder ordered = inOrder(topicRepository, deleteTopicUseCase, relationshipService);
        ordered.verify(topicRepository).deleteAllTopicSubjectLinksByTopicId("topic-1");
        ordered.verify(topicRepository).deleteAllLessonObjectiveTopicLinksByTopicId("topic-1");
        ordered.verify(topicRepository).deleteAllMaterialTopicLinksByTopicId("topic-1");
        ordered.verify(topicRepository).deleteAllTopicLessonLinksByTopicId("topic-1");
        ordered.verify(topicRepository).deleteAllLessonGroupPlanTopicLinksByTopicId("topic-1");
        ordered.verify(deleteTopicUseCase).deleteById("topic-1");
        ordered.verify(relationshipService).deleteAllRelationshipsForResource("topic", "topic-1");
        ordered.verify(relationshipService).deleteAllRelationshipsForResource("topic_user_group", "topic-1");
    }

    @DisplayName("Should return delete-impact preview by delegating to the use case")
    @Test
    void getDeleteImpact_delegatesToUseCase() {
        DeleteImpact impact = DeleteImpact.builder()
                .resourceType("Topic")
                .resourceId("topic-1")
                .affectedGroups(List.of(
                        AffectedResourceGroup.builder()
                                .resourceType("Material")
                                .cleanupAction(CleanupAction.DELETE)
                                .affectedIds(List.of("material-1", "material-2"))
                                .build()))
                .build();
        when(getTopicDeleteImpactUseCase.getDeleteImpact("topic-1")).thenReturn(impact);

        DeleteImpact result = topicService.getDeleteImpact("topic-1");

        assertThat(result).isSameAs(impact);
    }
}
