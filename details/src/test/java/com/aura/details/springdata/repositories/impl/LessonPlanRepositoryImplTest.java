package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.lessons.LessonGroupPlan;
import com.aura.details.springdata.models.lessons.LessonGroupModel;
import com.aura.details.springdata.models.lessons.LessonGroupPlanModel;
import com.aura.details.springdata.repositories.jpa.lessons.JpaLessonGroupPlanRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
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

@DisplayName("Lesson group plan repository implementation")
@ExtendWith(MockitoExtension.class)
class LessonPlanRepositoryImplTest {

    @Mock
    private JpaLessonGroupPlanRepository jpaLessonGroupPlanRepository;
    @Mock
    private EntityManager entityManager;
    @InjectMocks
    private LessonGroupPlanRepositoryImpl lessonGroupPlanRepositoryImpl;

    @DisplayName("Should attach managed lesson group proxy via EntityManager.getReference and persist on save")
    @Test
    void save_mapsDomainToModelAndBack() {
        UUID id = UUID.randomUUID();
        UUID lessonGroupId = UUID.randomUUID();
        LessonGroupPlan domain = LessonGroupPlan.builder()
                .lessonGroupId(lessonGroupId.toString())
                .plan("Some plan text")
                .tenantId("tenant-1")
                .build();

        LessonGroupModel lessonGroupProxy = LessonGroupModel.builder().id(lessonGroupId).build();
        when(entityManager.getReference(LessonGroupModel.class, lessonGroupId)).thenReturn(lessonGroupProxy);

        LessonGroupPlanModel savedModel = LessonGroupPlanModel.builder()
                .id(id)
                .lessonGroup(LessonGroupModel.builder().id(lessonGroupId).build())
                .plan("Some plan text")
                .tenantId("tenant-1")
                .build();
        when(jpaLessonGroupPlanRepository.save(any(LessonGroupPlanModel.class))).thenReturn(savedModel);

        LessonGroupPlan result = lessonGroupPlanRepositoryImpl.save(domain);

        ArgumentCaptor<LessonGroupPlanModel> captor = ArgumentCaptor.forClass(LessonGroupPlanModel.class);
        verify(jpaLessonGroupPlanRepository).save(captor.capture());
        assertThat(captor.getValue().getLessonGroup()).isSameAs(lessonGroupProxy);
        assertThat(captor.getValue().getPlan()).isEqualTo("Some plan text");
        assertThat(captor.getValue().getTenantId()).isEqualTo("tenant-1");

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id.toString());
        assertThat(result.getLessonGroupId()).isEqualTo(lessonGroupId.toString());
        assertThat(result.getPlan()).isEqualTo("Some plan text");
        assertThat(result.getTenantId()).isEqualTo("tenant-1");
    }

    @DisplayName("Should return the lesson group plan found by UUID id")
    @Test
    void findById_returnsLessonGroupPlan() {
        UUID id = UUID.randomUUID();
        UUID lessonGroupId = UUID.randomUUID();
        LessonGroupPlanModel model = LessonGroupPlanModel.builder()
                .id(id)
                .lessonGroup(LessonGroupModel.builder().id(lessonGroupId).build())
                .plan("plan")
                .build();
        when(jpaLessonGroupPlanRepository.findById(id)).thenReturn(Optional.of(model));

        LessonGroupPlan result = lessonGroupPlanRepositoryImpl.findById(id);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id.toString());
        assertThat(result.getLessonGroupId()).isEqualTo(lessonGroupId.toString());
        assertThat(result.getPlan()).isEqualTo("plan");
    }

    @DisplayName("Should return null when lesson group plan not found by id")
    @Test
    void findById_returnsNullWhenMissing() {
        UUID id = UUID.randomUUID();
        when(jpaLessonGroupPlanRepository.findById(id)).thenReturn(Optional.empty());

        assertThat(lessonGroupPlanRepositoryImpl.findById(id)).isNull();
    }

    @DisplayName("Should return the lesson group plan found by lessonGroupId")
    @Test
    void findByLessonGroupId_returnsLessonGroupPlan() {
        UUID id = UUID.randomUUID();
        UUID lessonGroupId = UUID.randomUUID();
        LessonGroupPlanModel model = LessonGroupPlanModel.builder()
                .id(id)
                .lessonGroup(LessonGroupModel.builder().id(lessonGroupId).build())
                .plan("plan")
                .build();
        when(jpaLessonGroupPlanRepository.findByLessonGroupId(lessonGroupId)).thenReturn(Optional.of(model));

        LessonGroupPlan result = lessonGroupPlanRepositoryImpl.findByLessonGroupId(lessonGroupId.toString());

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id.toString());
        assertThat(result.getLessonGroupId()).isEqualTo(lessonGroupId.toString());
    }

    @DisplayName("Should return null when no lesson group plan found for the given lessonGroupId")
    @Test
    void findByLessonGroupId_returnsNullWhenMissing() {
        UUID lessonGroupId = UUID.randomUUID();
        when(jpaLessonGroupPlanRepository.findByLessonGroupId(lessonGroupId)).thenReturn(Optional.empty());

        assertThat(lessonGroupPlanRepositoryImpl.findByLessonGroupId(lessonGroupId.toString())).isNull();
    }

    @DisplayName("Should return all lesson group plans for the given ids")
    @Test
    void findAllByIds_returnsMappedLessonGroupPlans() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        UUID group1 = UUID.randomUUID();
        UUID group2 = UUID.randomUUID();
        LessonGroupPlanModel m1 = LessonGroupPlanModel.builder()
                .id(id1)
                .lessonGroup(LessonGroupModel.builder().id(group1).build())
                .plan("p1")
                .build();
        LessonGroupPlanModel m2 = LessonGroupPlanModel.builder()
                .id(id2)
                .lessonGroup(LessonGroupModel.builder().id(group2).build())
                .plan("p2")
                .build();
        when(jpaLessonGroupPlanRepository.findAllById(List.of(id1, id2))).thenReturn(List.of(m1, m2));

        List<LessonGroupPlan> result = lessonGroupPlanRepositoryImpl.findAllByIds(List.of(id1, id2));

        assertThat(result).hasSize(2);
        assertThat(result).extracting(LessonGroupPlan::getId)
                .containsExactly(id1.toString(), id2.toString());
        assertThat(result).extracting(LessonGroupPlan::getLessonGroupId)
                .containsExactly(group1.toString(), group2.toString());
    }

    @DisplayName("Should return empty list when no plans match the given ids")
    @Test
    void findAllByIds_returnsEmptyWhenNoMatches() {
        UUID id = UUID.randomUUID();
        when(jpaLessonGroupPlanRepository.findAllById(List.of(id))).thenReturn(List.of());

        assertThat(lessonGroupPlanRepositoryImpl.findAllByIds(List.of(id))).isEmpty();
    }

    @DisplayName("Should group plans by lessonGroupId when querying by lesson group ids")
    @Test
    void findAllByLessonGroupIds_groupsByLessonGroup() {
        UUID group1 = UUID.randomUUID();
        UUID group2 = UUID.randomUUID();
        UUID plan1 = UUID.randomUUID();
        UUID plan2 = UUID.randomUUID();

        LessonGroupPlanModel m1 = LessonGroupPlanModel.builder().id(plan1)
                .lessonGroup(LessonGroupModel.builder().id(group1).build()).plan("p1").build();
        LessonGroupPlanModel m2 = LessonGroupPlanModel.builder().id(plan2)
                .lessonGroup(LessonGroupModel.builder().id(group2).build()).plan("p2").build();

        when(jpaLessonGroupPlanRepository.findAllByLessonGroupIdIn(List.of(group1, group2)))
                .thenReturn(List.of(m1, m2));

        var result = lessonGroupPlanRepositoryImpl.findAllByLessonGroupIds(List.of(group1.toString(), group2.toString()));

        assertThat(result).hasSize(2);
        assertThat(result.get(group1.toString()).getId()).isEqualTo(plan1.toString());
        assertThat(result.get(group2.toString()).getId()).isEqualTo(plan2.toString());
    }

    @DisplayName("Should return empty map when no lesson group ids provided")
    @Test
    void findAllByLessonGroupIds_returnsEmptyForEmptyInput() {
        assertThat(lessonGroupPlanRepositoryImpl.findAllByLessonGroupIds(List.of())).isEmpty();
    }
}
