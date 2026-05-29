package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.topics.Topic;
import com.aura.details.springdata.models.topics.TopicModel;
import com.aura.details.springdata.repositories.jpa.links.JpaTopicLessonLinkRepository;
import com.aura.details.springdata.repositories.jpa.topics.JpaTopicRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("Topic repository impl")
@ExtendWith(MockitoExtension.class)
class TopicRepositoryImplTest {

    @Mock
    private JpaTopicRepository jpaTopicRepository;
    @Mock
    private JpaTopicLessonLinkRepository jpaTopicLessonLinkRepository;
    @InjectMocks
    private TopicRepositoryImpl repository;

    @DisplayName("Should return the topic found by id")
    @Test
    void findById_returnsTopic() {
        UUID id = UUID.randomUUID();
        TopicModel model = TopicModel.builder().id(id).name("Algebra").build();
        when(jpaTopicRepository.findById(id)).thenReturn(Optional.of(model));
        assertThat(repository.findById(id.toString()).getName()).isEqualTo("Algebra");
    }

    @DisplayName("Should return null when topic not found by id")
    @Test
    void findById_returnsNullWhenMissing() {
        UUID id = UUID.randomUUID();
        when(jpaTopicRepository.findById(id)).thenReturn(Optional.empty());
        assertThat(repository.findById(id.toString())).isNull();
    }

    @DisplayName("Should return all topics for the tenant id")
    @Test
    void findAllByTenantId_returnsTopics() {
        TopicModel t1 = TopicModel.builder().id(UUID.randomUUID()).name("Algebra").build();
        TopicModel t2 = TopicModel.builder().id(UUID.randomUUID()).name("Geometry").build();
        when(jpaTopicRepository.findByTenantId("t1")).thenReturn(List.of(t1, t2));

        List<Topic> result = repository.findAllByTenantId("t1");
        assertThat(result).extracting(Topic::getName).containsExactly("Algebra", "Geometry");
    }

    @DisplayName("Should return empty list when no topics in tenant")
    @Test
    void findAllByTenantId_returnsEmpty() {
        when(jpaTopicRepository.findByTenantId("t1")).thenReturn(List.of());
        assertThat(repository.findAllByTenantId("t1")).isEmpty();
    }

    @DisplayName("Should group topics by lessonId when querying by lesson ids")
    @Test
    void findAllByLessonIds_groupsByLesson() {
        UUID lesson1 = UUID.randomUUID();
        UUID lesson2 = UUID.randomUUID();
        TopicModel t1 = TopicModel.builder().id(UUID.randomUUID()).name("Algebra").build();
        TopicModel t2 = TopicModel.builder().id(UUID.randomUUID()).name("Geometry").build();
        TopicModel t3 = TopicModel.builder().id(UUID.randomUUID()).name("Calculus").build();
        when(jpaTopicLessonLinkRepository.findTopicsByLessonIds(List.of(lesson1, lesson2)))
                .thenReturn(List.of(
                        new Object[]{lesson1, t1},
                        new Object[]{lesson1, t2},
                        new Object[]{lesson2, t3}));

        var result = repository.findAllByLessonIds(List.of(lesson1.toString(), lesson2.toString()));

        assertThat(result.get(lesson1.toString())).extracting(Topic::getName)
                .containsExactlyInAnyOrder("Algebra", "Geometry");
        assertThat(result.get(lesson2.toString())).extracting(Topic::getName)
                .containsExactly("Calculus");
    }

    @DisplayName("Should return empty map when no lesson ids provided")
    @Test
    void findAllByLessonIds_returnsEmptyForEmptyInput() {
        assertThat(repository.findAllByLessonIds(List.of())).isEmpty();
    }
}
