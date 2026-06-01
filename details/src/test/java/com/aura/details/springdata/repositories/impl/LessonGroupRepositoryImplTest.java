package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.lessons.LessonGroup;
import com.aura.details.springdata.models.lessons.LessonGroupModel;
import com.aura.details.springdata.models.lessons.LessonModel;
import com.aura.details.springdata.repositories.jpa.lessons.JpaLessonGroupRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("LessonGroupRepositoryImpl")
@ExtendWith(MockitoExtension.class)
class LessonGroupRepositoryImplTest {

    @Mock
    private JpaLessonGroupRepository jpaLessonGroupRepository;
    @Mock
    private EntityManager entityManager;
    @InjectMocks
    private LessonGroupRepositoryImpl lessonGroupRepositoryImpl;

    @DisplayName("Should save lesson group via JPA repo with lesson ref attached")
    @Test
    void save_attachesLessonRefAndPersists() {
        UUID lessonId = UUID.randomUUID();
        UUID groupId = UUID.randomUUID();
        LessonGroup domain = LessonGroup.builder()
                .name("Group A")
                .lessonId(lessonId.toString())
                .tenantId("tenant-1")
                .build();

        LessonModel lessonProxy = LessonModel.builder().id(lessonId).build();
        when(entityManager.getReference(LessonModel.class, lessonId)).thenReturn(lessonProxy);

        LessonGroupModel savedModel = LessonGroupModel.builder()
                .id(groupId)
                .lesson(LessonModel.builder().id(lessonId).build())
                .name("Group A")
                .tenantId("tenant-1")
                .build();
        when(jpaLessonGroupRepository.save(any(LessonGroupModel.class))).thenReturn(savedModel);

        LessonGroup result = lessonGroupRepositoryImpl.save(domain);

        ArgumentCaptor<LessonGroupModel> captor = ArgumentCaptor.forClass(LessonGroupModel.class);
        verify(jpaLessonGroupRepository).save(captor.capture());
        assertThat(captor.getValue().getLesson()).isSameAs(lessonProxy);
        assertThat(captor.getValue().getName()).isEqualTo("Group A");
        assertThat(captor.getValue().getTenantId()).isEqualTo("tenant-1");

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(groupId.toString());
        assertThat(result.getLessonId()).isEqualTo(lessonId.toString());
        assertThat(result.getName()).isEqualTo("Group A");
    }

    @DisplayName("Should return null when findById misses")
    @Test
    void findById_returnsNullWhenMissing() {
        UUID id = UUID.randomUUID();
        when(jpaLessonGroupRepository.findById(id)).thenReturn(Optional.empty());

        assertThat(lessonGroupRepositoryImpl.findById(id)).isNull();
    }

    @DisplayName("Should return mapped entity when findById hits")
    @Test
    void findById_returnsMappedEntity() {
        UUID id = UUID.randomUUID();
        UUID lessonId = UUID.randomUUID();
        LessonGroupModel model = LessonGroupModel.builder()
                .id(id)
                .lesson(LessonModel.builder().id(lessonId).build())
                .name("Group B")
                .tenantId("tenant-2")
                .build();
        when(jpaLessonGroupRepository.findById(id)).thenReturn(Optional.of(model));

        LessonGroup result = lessonGroupRepositoryImpl.findById(id);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id.toString());
        assertThat(result.getLessonId()).isEqualTo(lessonId.toString());
        assertThat(result.getName()).isEqualTo("Group B");
        assertThat(result.getTenantId()).isEqualTo("tenant-2");
    }

    @DisplayName("Should map findAllByLessonId results")
    @Test
    void findAllByLessonId_returnsMappedList() {
        UUID lessonId = UUID.randomUUID();
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        LessonModel lessonModel = LessonModel.builder().id(lessonId).build();
        LessonGroupModel m1 = LessonGroupModel.builder().id(id1).lesson(lessonModel).name("G1").tenantId("t1").build();
        LessonGroupModel m2 = LessonGroupModel.builder().id(id2).lesson(lessonModel).name("G2").tenantId("t1").build();
        when(jpaLessonGroupRepository.findAllByLessonId(lessonId)).thenReturn(List.of(m1, m2));

        List<LessonGroup> result = lessonGroupRepositoryImpl.findAllByLessonId(lessonId.toString());

        assertThat(result).hasSize(2);
        assertThat(result).extracting(LessonGroup::getId)
                .containsExactly(id1.toString(), id2.toString());
        assertThat(result).extracting(LessonGroup::getLessonId)
                .containsOnly(lessonId.toString());
    }

    @DisplayName("Should group findAllByLessonIds by lessonId")
    @Test
    void findAllByLessonIds_groupsByLessonId() {
        UUID lessonId1 = UUID.randomUUID();
        UUID lessonId2 = UUID.randomUUID();
        UUID groupId1 = UUID.randomUUID();
        UUID groupId2 = UUID.randomUUID();
        UUID groupId3 = UUID.randomUUID();

        LessonModel lesson1 = LessonModel.builder().id(lessonId1).build();
        LessonModel lesson2 = LessonModel.builder().id(lessonId2).build();

        LessonGroupModel m1 = LessonGroupModel.builder().id(groupId1).lesson(lesson1).name("G1").tenantId("t1").build();
        LessonGroupModel m2 = LessonGroupModel.builder().id(groupId2).lesson(lesson1).name("G2").tenantId("t1").build();
        LessonGroupModel m3 = LessonGroupModel.builder().id(groupId3).lesson(lesson2).name("G3").tenantId("t1").build();

        when(jpaLessonGroupRepository.findAllByLessonIdIn(List.of(lessonId1, lessonId2)))
                .thenReturn(List.of(m1, m2, m3));

        Map<String, List<LessonGroup>> result = lessonGroupRepositoryImpl
                .findAllByLessonIds(List.of(lessonId1.toString(), lessonId2.toString()));

        assertThat(result).hasSize(2);
        assertThat(result.get(lessonId1.toString())).hasSize(2);
        assertThat(result.get(lessonId1.toString())).extracting(LessonGroup::getId)
                .containsExactlyInAnyOrder(groupId1.toString(), groupId2.toString());
        assertThat(result.get(lessonId2.toString())).hasSize(1);
        assertThat(result.get(lessonId2.toString()).get(0).getId()).isEqualTo(groupId3.toString());
    }

    @DisplayName("Should return empty map when lessonIds input is empty")
    @Test
    void findAllByLessonIds_returnsEmptyMapForEmptyInput() {
        Map<String, List<LessonGroup>> result = lessonGroupRepositoryImpl.findAllByLessonIds(List.of());

        assertThat(result).isEmpty();
    }
}
