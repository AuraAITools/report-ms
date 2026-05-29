package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.links.LevelLessonLink;
import com.aura.details.springdata.models.lessons.LessonModel;
import com.aura.details.springdata.models.levels.LevelModel;
import com.aura.details.springdata.models.links.LevelLessonLinkModel;
import com.aura.details.springdata.repositories.jpa.links.JpaLevelLessonLinkRepository;
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

@DisplayName("Level lesson link repository impl")
@ExtendWith(MockitoExtension.class)
class LevelLessonLinkRepositoryImplTest {

    @Mock
    private JpaLevelLessonLinkRepository jpaRepository;
    @Mock
    private EntityManager entityManager;
    @InjectMocks
    private LevelLessonLinkRepositoryImpl repository;

    @DisplayName("Should save a single level-lesson link")
    @Test
    void saveDelegatesToJpaRepository() {
        LevelLessonLinkModel savedModel = new LevelLessonLinkModel();
        when(jpaRepository.save(any(LevelLessonLinkModel.class))).thenReturn(savedModel);

        LevelLessonLink domain = LevelLessonLink.builder().build();
        LevelLessonLink result = repository.save(domain);

        assertThat(result).isNotNull();
        verify(jpaRepository).save(any(LevelLessonLinkModel.class));
    }

    @DisplayName("Should save all level-lesson links")
    @Test
    void saveAllDelegatesToJpaRepository() {
        LevelLessonLinkModel model1 = new LevelLessonLinkModel();
        LevelLessonLinkModel model2 = new LevelLessonLinkModel();
        when(jpaRepository.saveAll(anyList())).thenReturn(List.of(model1, model2));

        List<LevelLessonLink> domains = List.of(
                LevelLessonLink.builder().build(),
                LevelLessonLink.builder().build());
        List<LevelLessonLink> result = repository.saveAll(domains);

        assertThat(result).hasSize(2);
        verify(jpaRepository).saveAll(anyList());
    }

    @DisplayName("Should attach managed level and lesson proxies via EntityManager.getReference before persisting")
    @Test
    void saveAttachesLevelAndLessonRefs() {
        UUID levelId = UUID.randomUUID();
        UUID lessonId = UUID.randomUUID();
        LevelModel levelProxy = LevelModel.builder().id(levelId).build();
        LessonModel lessonProxy = LessonModel.builder().id(lessonId).build();
        when(entityManager.getReference(LevelModel.class, levelId)).thenReturn(levelProxy);
        when(entityManager.getReference(LessonModel.class, lessonId)).thenReturn(lessonProxy);
        when(jpaRepository.save(any(LevelLessonLinkModel.class))).thenReturn(new LevelLessonLinkModel());

        LevelLessonLink domain = LevelLessonLink.builder()
                .levelId(levelId.toString())
                .lessonId(lessonId.toString())
                .build();
        repository.save(domain);

        ArgumentCaptor<LevelLessonLinkModel> captor = ArgumentCaptor.forClass(LevelLessonLinkModel.class);
        verify(jpaRepository).save(captor.capture());
        assertThat(captor.getValue().getLevel()).isSameAs(levelProxy);
        assertThat(captor.getValue().getLesson()).isSameAs(lessonProxy);
    }
}
