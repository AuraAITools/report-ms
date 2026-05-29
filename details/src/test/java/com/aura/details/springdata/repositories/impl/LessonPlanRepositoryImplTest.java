package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.lessons.LessonPlan;
import com.aura.details.springdata.models.lessons.LessonModel;
import com.aura.details.springdata.models.lessons.LessonPlanModel;
import com.aura.details.springdata.repositories.jpa.lessons.JpaLessonPlanRepository;
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

@DisplayName("Lesson plan repository implementation")
@ExtendWith(MockitoExtension.class)
class LessonPlanRepositoryImplTest {

    @Mock
    private JpaLessonPlanRepository jpaLessonPlanRepository;
    @Mock
    private EntityManager entityManager;
    @InjectMocks
    private LessonPlanRepositoryImpl lessonPlanRepositoryImpl;

    @DisplayName("Should attach managed lesson proxy via EntityManager.getReference and persist on save")
    @Test
    void save_mapsDomainToModelAndBack() {
        UUID id = UUID.randomUUID();
        UUID lessonId = UUID.randomUUID();
        LessonPlan domain = LessonPlan.builder()
                .lessonId(lessonId.toString())
                .plan("Some plan text")
                .tenantId("tenant-1")
                .build();

        LessonModel lessonProxy = LessonModel.builder().id(lessonId).build();
        when(entityManager.getReference(LessonModel.class, lessonId)).thenReturn(lessonProxy);

        LessonPlanModel savedModel = LessonPlanModel.builder()
                .id(id)
                .lesson(LessonModel.builder().id(lessonId).build())
                .plan("Some plan text")
                .tenantId("tenant-1")
                .build();
        when(jpaLessonPlanRepository.save(any(LessonPlanModel.class))).thenReturn(savedModel);

        LessonPlan result = lessonPlanRepositoryImpl.save(domain);

        ArgumentCaptor<LessonPlanModel> captor = ArgumentCaptor.forClass(LessonPlanModel.class);
        verify(jpaLessonPlanRepository).save(captor.capture());
        assertThat(captor.getValue().getLesson()).isSameAs(lessonProxy);
        assertThat(captor.getValue().getPlan()).isEqualTo("Some plan text");
        assertThat(captor.getValue().getTenantId()).isEqualTo("tenant-1");

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id.toString());
        assertThat(result.getLessonId()).isEqualTo(lessonId.toString());
        assertThat(result.getPlan()).isEqualTo("Some plan text");
        assertThat(result.getTenantId()).isEqualTo("tenant-1");
    }

    @DisplayName("Should return the lesson plan found by UUID id")
    @Test
    void findById_returnsLessonPlan() {
        UUID id = UUID.randomUUID();
        UUID lessonId = UUID.randomUUID();
        LessonPlanModel model = LessonPlanModel.builder()
                .id(id)
                .lesson(LessonModel.builder().id(lessonId).build())
                .plan("plan")
                .build();
        when(jpaLessonPlanRepository.findById(id)).thenReturn(Optional.of(model));

        LessonPlan result = lessonPlanRepositoryImpl.findById(id);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id.toString());
        assertThat(result.getLessonId()).isEqualTo(lessonId.toString());
        assertThat(result.getPlan()).isEqualTo("plan");
    }

    @DisplayName("Should return null when lesson plan not found by id")
    @Test
    void findById_returnsNullWhenMissing() {
        UUID id = UUID.randomUUID();
        when(jpaLessonPlanRepository.findById(id)).thenReturn(Optional.empty());

        assertThat(lessonPlanRepositoryImpl.findById(id)).isNull();
    }

    @DisplayName("Should convert lessonId string to UUID and return all matching lesson plans")
    @Test
    void findAllByLessonId_returnsLessonPlans() {
        UUID lessonId = UUID.randomUUID();
        UUID planId1 = UUID.randomUUID();
        UUID planId2 = UUID.randomUUID();
        LessonPlanModel model1 = LessonPlanModel.builder()
                .id(planId1)
                .lesson(LessonModel.builder().id(lessonId).build())
                .plan("plan 1")
                .build();
        LessonPlanModel model2 = LessonPlanModel.builder()
                .id(planId2)
                .lesson(LessonModel.builder().id(lessonId).build())
                .plan("plan 2")
                .build();
        when(jpaLessonPlanRepository.findAllByLessonId(lessonId)).thenReturn(java.util.List.of(model1, model2));

        java.util.List<LessonPlan> result = lessonPlanRepositoryImpl.findAllByLessonId(lessonId.toString());

        assertThat(result).hasSize(2);
        assertThat(result).extracting(LessonPlan::getId)
                .containsExactlyInAnyOrder(planId1.toString(), planId2.toString());
    }

    @DisplayName("Should return empty list when no lesson plan exists for the given lesson id")
    @Test
    void findAllByLessonId_returnsEmptyWhenMissing() {
        UUID lessonId = UUID.randomUUID();
        when(jpaLessonPlanRepository.findAllByLessonId(lessonId)).thenReturn(java.util.List.of());

        assertThat(lessonPlanRepositoryImpl.findAllByLessonId(lessonId.toString())).isEmpty();
    }

    @DisplayName("Should return all lesson plans for the given ids")
    @Test
    void findAllByIds_returnsMappedLessonPlans() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        UUID lesson1 = UUID.randomUUID();
        UUID lesson2 = UUID.randomUUID();
        LessonPlanModel m1 = LessonPlanModel.builder()
                .id(id1)
                .lesson(LessonModel.builder().id(lesson1).build())
                .plan("p1")
                .build();
        LessonPlanModel m2 = LessonPlanModel.builder()
                .id(id2)
                .lesson(LessonModel.builder().id(lesson2).build())
                .plan("p2")
                .build();
        when(jpaLessonPlanRepository.findAllById(List.of(id1, id2))).thenReturn(List.of(m1, m2));

        List<LessonPlan> result = lessonPlanRepositoryImpl.findAllByIds(List.of(id1, id2));

        assertThat(result).hasSize(2);
        assertThat(result).extracting(LessonPlan::getId)
                .containsExactly(id1.toString(), id2.toString());
        assertThat(result).extracting(LessonPlan::getLessonId)
                .containsExactly(lesson1.toString(), lesson2.toString());
    }

    @DisplayName("Should return empty list when no plans match the given ids")
    @Test
    void findAllByIds_returnsEmptyWhenNoMatches() {
        UUID id = UUID.randomUUID();
        when(jpaLessonPlanRepository.findAllById(List.of(id))).thenReturn(List.of());

        assertThat(lessonPlanRepositoryImpl.findAllByIds(List.of(id))).isEmpty();
    }

    @DisplayName("Should group plans by lessonId when querying by lesson ids")
    @Test
    void findAllByLessonIds_groupsByLesson() {
        UUID lesson1 = UUID.randomUUID();
        UUID lesson2 = UUID.randomUUID();
        UUID plan1 = UUID.randomUUID();
        UUID plan2 = UUID.randomUUID();
        UUID plan3 = UUID.randomUUID();

        LessonPlanModel m1 = LessonPlanModel.builder().id(plan1)
                .lesson(LessonModel.builder().id(lesson1).build()).plan("p1").build();
        LessonPlanModel m2 = LessonPlanModel.builder().id(plan2)
                .lesson(LessonModel.builder().id(lesson1).build()).plan("p2").build();
        LessonPlanModel m3 = LessonPlanModel.builder().id(plan3)
                .lesson(LessonModel.builder().id(lesson2).build()).plan("p3").build();

        when(jpaLessonPlanRepository.findAllByLessonIdIn(List.of(lesson1, lesson2)))
                .thenReturn(List.of(m1, m2, m3));

        var result = lessonPlanRepositoryImpl.findAllByLessonIds(List.of(lesson1.toString(), lesson2.toString()));

        assertThat(result.get(lesson1.toString())).extracting(LessonPlan::getId)
                .containsExactlyInAnyOrder(plan1.toString(), plan2.toString());
        assertThat(result.get(lesson2.toString())).extracting(LessonPlan::getId)
                .containsExactly(plan3.toString());
    }

    @DisplayName("Should return empty map when no lesson ids provided")
    @Test
    void findAllByLessonIds_returnsEmptyForEmptyInput() {
        assertThat(lessonPlanRepositoryImpl.findAllByLessonIds(List.of())).isEmpty();
    }
}
