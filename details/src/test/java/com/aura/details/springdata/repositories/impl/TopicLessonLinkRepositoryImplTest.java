package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.links.TopicLessonLink;
import com.aura.details.springdata.models.lessons.LessonModel;
import com.aura.details.springdata.models.links.TopicLessonLinkModel;
import com.aura.details.springdata.models.topics.TopicModel;
import com.aura.details.springdata.repositories.jpa.links.JpaTopicLessonLinkRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Topic lesson link repository impl")
@ExtendWith(MockitoExtension.class)
class TopicLessonLinkRepositoryImplTest {

    @Mock
    private JpaTopicLessonLinkRepository jpaRepository;
    @Mock
    private EntityManager entityManager;
    @InjectMocks
    private TopicLessonLinkRepositoryImpl repository;

    @DisplayName("Should save a single topic-lesson link")
    @Test
    void saveDelegatesToJpaRepository() {
        TopicLessonLinkModel savedModel = new TopicLessonLinkModel();
        when(jpaRepository.save(any(TopicLessonLinkModel.class))).thenReturn(savedModel);

        TopicLessonLink domain = TopicLessonLink.builder().build();
        TopicLessonLink result = repository.save(domain);

        assertThat(result).isNotNull();
        verify(jpaRepository).save(any(TopicLessonLinkModel.class));
    }

    @DisplayName("Should save all topic-lesson links")
    @Test
    void saveAllDelegatesToJpaRepository() {
        TopicLessonLinkModel model1 = new TopicLessonLinkModel();
        TopicLessonLinkModel model2 = new TopicLessonLinkModel();
        when(jpaRepository.saveAll(anyList())).thenReturn(List.of(model1, model2));

        List<TopicLessonLink> domains = List.of(
                TopicLessonLink.builder().build(),
                TopicLessonLink.builder().build());
        List<TopicLessonLink> result = repository.saveAll(domains);

        assertThat(result).hasSize(2);
        verify(jpaRepository).saveAll(anyList());
    }

    @DisplayName("Should attach managed topic and lesson proxies via EntityManager.getReference before persisting")
    @Test
    void saveAttachesTopicAndLessonRefs() {
        UUID topicId = UUID.randomUUID();
        UUID lessonId = UUID.randomUUID();
        TopicModel topicProxy = TopicModel.builder().id(topicId).build();
        LessonModel lessonProxy = LessonModel.builder().id(lessonId).build();
        when(entityManager.getReference(TopicModel.class, topicId)).thenReturn(topicProxy);
        when(entityManager.getReference(LessonModel.class, lessonId)).thenReturn(lessonProxy);
        when(jpaRepository.save(any(TopicLessonLinkModel.class))).thenReturn(new TopicLessonLinkModel());

        TopicLessonLink domain = TopicLessonLink.builder()
                .topicId(topicId.toString())
                .lessonId(lessonId.toString())
                .build();
        repository.save(domain);

        ArgumentCaptor<TopicLessonLinkModel> captor = ArgumentCaptor.forClass(TopicLessonLinkModel.class);
        verify(jpaRepository).save(captor.capture());
        assertThat(captor.getValue().getTopic()).isSameAs(topicProxy);
        assertThat(captor.getValue().getLesson()).isSameAs(lessonProxy);
    }
}
