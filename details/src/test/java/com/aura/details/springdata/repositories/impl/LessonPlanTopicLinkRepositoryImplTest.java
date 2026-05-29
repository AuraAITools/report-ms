package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.links.LessonPlanTopicLink;
import com.aura.core.entities.topics.Topic;
import com.aura.details.springdata.models.lessons.LessonPlanModel;
import com.aura.details.springdata.models.links.LessonPlanTopicLinkModel;
import com.aura.details.springdata.models.topics.TopicModel;
import com.aura.details.springdata.repositories.jpa.links.JpaLessonPlanTopicLinkRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@DisplayName("Lesson plan topic link repository implementation")
@ExtendWith(MockitoExtension.class)
class LessonPlanTopicLinkRepositoryImplTest {

    @Mock
    private JpaLessonPlanTopicLinkRepository jpaLessonPlanTopicLinkRepository;
    @Mock
    private EntityManager entityManager;
    @InjectMocks
    private LessonPlanTopicLinkRepositoryImpl lessonPlanTopicLinkRepositoryImpl;

    @DisplayName("Should attach managed lesson plan and topic proxies via EntityManager.getReference before persisting")
    @Test
    void saveAll_mapsAndPersistsAll() {
        UUID planId = UUID.randomUUID();
        UUID topicId1 = UUID.randomUUID();
        UUID topicId2 = UUID.randomUUID();

        LessonPlanModel planProxy = LessonPlanModel.builder().id(planId).build();
        TopicModel topic1Proxy = TopicModel.builder().id(topicId1).build();
        TopicModel topic2Proxy = TopicModel.builder().id(topicId2).build();
        when(entityManager.getReference(LessonPlanModel.class, planId)).thenReturn(planProxy);
        when(entityManager.getReference(TopicModel.class, topicId1)).thenReturn(topic1Proxy);
        when(entityManager.getReference(TopicModel.class, topicId2)).thenReturn(topic2Proxy);

        LessonPlanTopicLink link1 = LessonPlanTopicLink.builder()
                .lessonPlanId(planId.toString())
                .topicId(topicId1.toString())
                .tenantId("tenant-1")
                .build();
        LessonPlanTopicLink link2 = LessonPlanTopicLink.builder()
                .lessonPlanId(planId.toString())
                .topicId(topicId2.toString())
                .tenantId("tenant-1")
                .build();

        LessonPlanTopicLinkModel savedModel1 = LessonPlanTopicLinkModel.builder()
                .id(UUID.randomUUID())
                .lessonPlan(LessonPlanModel.builder().id(planId).build())
                .topic(TopicModel.builder().id(topicId1).build())
                .tenantId("tenant-1")
                .build();
        LessonPlanTopicLinkModel savedModel2 = LessonPlanTopicLinkModel.builder()
                .id(UUID.randomUUID())
                .lessonPlan(LessonPlanModel.builder().id(planId).build())
                .topic(TopicModel.builder().id(topicId2).build())
                .tenantId("tenant-1")
                .build();

        when(jpaLessonPlanTopicLinkRepository.<LessonPlanTopicLinkModel>saveAll(org.mockito.ArgumentMatchers.anyList()))
                .thenReturn(List.of(savedModel1, savedModel2));

        List<LessonPlanTopicLink> result = lessonPlanTopicLinkRepositoryImpl.saveAll(List.of(link1, link2));

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<LessonPlanTopicLinkModel>> captor = ArgumentCaptor.forClass(List.class);
        verify(jpaLessonPlanTopicLinkRepository).saveAll(captor.capture());
        List<LessonPlanTopicLinkModel> persisted = captor.getValue();
        assertThat(persisted).hasSize(2);
        assertThat(persisted.get(0).getLessonPlan()).isSameAs(planProxy);
        assertThat(persisted.get(0).getTopic()).isSameAs(topic1Proxy);
        assertThat(persisted.get(1).getLessonPlan()).isSameAs(planProxy);
        assertThat(persisted.get(1).getTopic()).isSameAs(topic2Proxy);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(LessonPlanTopicLink::getLessonPlanId)
                .containsExactly(planId.toString(), planId.toString());
        assertThat(result).extracting(LessonPlanTopicLink::getTopicId)
                .containsExactly(topicId1.toString(), topicId2.toString());
    }

    @DisplayName("Should return empty list and skip JPA call when input list is empty")
    @Test
    void saveAll_emptyList_returnsEmpty() {
        List<LessonPlanTopicLink> result = lessonPlanTopicLinkRepositoryImpl.saveAll(List.of());

        assertThat(result).isEmpty();
        verifyNoInteractions(jpaLessonPlanTopicLinkRepository);
    }

    @DisplayName("Should group topics by lesson plan id from the JPQL link table query")
    @Test
    void findTopicsByLessonPlanIds_groupsTopicsByPlan() {
        UUID planId1 = UUID.randomUUID();
        UUID planId2 = UUID.randomUUID();
        UUID topicId1 = UUID.randomUUID();
        UUID topicId2 = UUID.randomUUID();
        UUID topicId3 = UUID.randomUUID();
        TopicModel t1 = TopicModel.builder().id(topicId1).build();
        TopicModel t2 = TopicModel.builder().id(topicId2).build();
        TopicModel t3 = TopicModel.builder().id(topicId3).build();

        when(jpaLessonPlanTopicLinkRepository.findTopicsByLessonPlanIds(List.of(planId1, planId2)))
                .thenReturn(List.of(
                        new Object[]{planId1, t1},
                        new Object[]{planId1, t2},
                        new Object[]{planId2, t3}
                ));

        Map<String, List<Topic>> result = lessonPlanTopicLinkRepositoryImpl
                .findTopicsByLessonPlanIds(List.of(planId1.toString(), planId2.toString()));

        assertThat(result).hasSize(2);
        assertThat(result.get(planId1.toString())).extracting(Topic::getId)
                .containsExactly(topicId1.toString(), topicId2.toString());
        assertThat(result.get(planId2.toString())).extracting(Topic::getId)
                .containsExactly(topicId3.toString());
    }

    @DisplayName("Should return empty map when lesson plan id list is empty")
    @Test
    void findTopicsByLessonPlanIds_emptyList_returnsEmptyMap() {
        assertThat(lessonPlanTopicLinkRepositoryImpl.findTopicsByLessonPlanIds(List.of())).isEmpty();
        verifyNoInteractions(jpaLessonPlanTopicLinkRepository);
    }

    @DisplayName("Should return map without an entry for plan ids that have no matching topics")
    @Test
    void findTopicsByLessonPlanIds_missingKey_skipsEntry() {
        UUID planId1 = UUID.randomUUID();
        UUID planId2 = UUID.randomUUID();
        UUID topicId = UUID.randomUUID();
        TopicModel topic = TopicModel.builder().id(topicId).build();

        when(jpaLessonPlanTopicLinkRepository.findTopicsByLessonPlanIds(List.of(planId1, planId2)))
                .thenReturn(List.<Object[]>of(new Object[]{planId1, topic}));

        Map<String, List<Topic>> result = lessonPlanTopicLinkRepositoryImpl
                .findTopicsByLessonPlanIds(List.of(planId1.toString(), planId2.toString()));

        assertThat(result).hasSize(1);
        assertThat(result).containsKey(planId1.toString());
        assertThat(result).doesNotContainKey(planId2.toString());
    }
}
