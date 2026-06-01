package com.aura.details.services;

import com.aura.core.entities.lessons.LessonGroup;
import com.aura.core.entities.lessons.LessonGroupPlan;
import com.aura.core.entities.lessons.LessonObjective;
import com.aura.core.entities.topics.Topic;
import com.aura.core.repositories.LessonGroupPlanRepository;
import com.aura.core.repositories.LessonGroupPlanTopicLinkRepository;
import com.aura.core.repositories.LessonGroupRepository;
import com.aura.core.repositories.TopicLessonLinkRepository;
import com.aura.core.usecases.lesson_group_plans.AddMaterialsToLessonGroupPlanUseCase;
import com.aura.core.usecases.lesson_group_plans.AddTopicsToLessonGroupPlanUseCase;
import com.aura.core.usecases.lesson_group_plans.CreateLessonGroupPlanUseCase;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@DisplayName("LessonGroupPlanService")
@ExtendWith(MockitoExtension.class)
class LessonPlanServiceTest {

    @Mock
    private CreateLessonGroupPlanUseCase createLessonGroupPlanUseCase;
    @Mock
    private AddTopicsToLessonGroupPlanUseCase addTopicsToLessonGroupPlanUseCase;
    @Mock
    private AddMaterialsToLessonGroupPlanUseCase addMaterialsToLessonGroupPlanUseCase;
    @Mock
    private LessonGroupPlanRepository lessonGroupPlanRepository;
    @Mock
    private LessonGroupPlanTopicLinkRepository lessonGroupPlanTopicLinkRepository;
    @Mock
    private TopicLessonLinkRepository topicLessonLinkRepository;
    @Mock
    private LessonGroupRepository lessonGroupRepository;
    @Mock
    private LessonObjectiveService lessonObjectiveService;

    @InjectMocks
    private LessonGroupPlanService lessonGroupPlanService;

    @DisplayName("Should delegate to both use cases and return the created plan when topicIds is non-empty")
    @Test
    void createLessonGroupPlan_withTopics_delegatesToBothUseCases() {
        UUID lessonGroupUuid = UUID.randomUUID();
        String lessonGroupId = lessonGroupUuid.toString();
        String lessonId = UUID.randomUUID().toString();
        String tenantId = "tenant-123";
        List<String> topicIds = List.of("topic-1", "topic-2");

        LessonGroup group = LessonGroup.builder().id(lessonGroupId).lessonId(lessonId).build();
        LessonGroupPlan input = LessonGroupPlan.builder().plan("My plan").build();
        LessonGroupPlan created = LessonGroupPlan.builder()
                .id("plan-999")
                .lessonGroupId(lessonGroupId)
                .tenantId(tenantId)
                .plan("My plan")
                .build();

        when(lessonGroupRepository.findById(lessonGroupUuid)).thenReturn(group);
        when(topicLessonLinkRepository.findTopicIdsByLessonId(lessonId))
                .thenReturn(Set.of("topic-1", "topic-2", "topic-3"));
        when(createLessonGroupPlanUseCase.createLessonGroupPlan(input, lessonGroupId)).thenReturn(created);
        when(addTopicsToLessonGroupPlanUseCase.addTopicsToLessonGroupPlan("plan-999", topicIds, tenantId))
                .thenReturn(List.of());

        LessonGroupPlan result = lessonGroupPlanService.createLessonGroupPlan(
                input, lessonGroupId, topicIds, List.of(), List.of());

        assertThat(result).isSameAs(created);
        verify(createLessonGroupPlanUseCase).createLessonGroupPlan(input, lessonGroupId);
        verify(addTopicsToLessonGroupPlanUseCase).addTopicsToLessonGroupPlan("plan-999", topicIds, tenantId);
        verifyNoInteractions(addMaterialsToLessonGroupPlanUseCase, lessonObjectiveService);
    }

    @DisplayName("Should skip addTopicsToLessonGroupPlanUseCase when topicIds is empty")
    @Test
    void createLessonGroupPlan_withEmptyTopics_skipsAddTopicsUseCase() {
        UUID lessonGroupUuid = UUID.randomUUID();
        String lessonGroupId = lessonGroupUuid.toString();
        String lessonId = UUID.randomUUID().toString();
        String tenantId = "tenant-123";

        LessonGroup group = LessonGroup.builder().id(lessonGroupId).lessonId(lessonId).build();
        LessonGroupPlan input = LessonGroupPlan.builder().plan("My plan").build();
        LessonGroupPlan created = LessonGroupPlan.builder()
                .id("plan-999")
                .lessonGroupId(lessonGroupId)
                .tenantId(tenantId)
                .plan("My plan")
                .build();

        when(lessonGroupRepository.findById(lessonGroupUuid)).thenReturn(group);
        when(createLessonGroupPlanUseCase.createLessonGroupPlan(input, lessonGroupId)).thenReturn(created);

        LessonGroupPlan result = lessonGroupPlanService.createLessonGroupPlan(
                input, lessonGroupId, List.of(), List.of(), List.of());

        assertThat(result).isSameAs(created);
        verify(createLessonGroupPlanUseCase).createLessonGroupPlan(input, lessonGroupId);
        verify(addTopicsToLessonGroupPlanUseCase, never())
                .addTopicsToLessonGroupPlan(eq("plan-999"), eq(List.of()), eq(tenantId));
        verifyNoInteractions(addTopicsToLessonGroupPlanUseCase, addMaterialsToLessonGroupPlanUseCase,
                lessonObjectiveService, topicLessonLinkRepository);
    }

    @DisplayName("Should reject creation when any topicId is not linked to the lesson")
    @Test
    void createLessonGroupPlan_withTopicNotInLesson_throws() {
        UUID lessonGroupUuid = UUID.randomUUID();
        String lessonGroupId = lessonGroupUuid.toString();
        String lessonId = UUID.randomUUID().toString();
        List<String> topicIds = List.of("topic-1", "topic-rogue");

        LessonGroup group = LessonGroup.builder().id(lessonGroupId).lessonId(lessonId).build();
        LessonGroupPlan input = LessonGroupPlan.builder().plan("My plan").build();

        when(lessonGroupRepository.findById(lessonGroupUuid)).thenReturn(group);
        when(topicLessonLinkRepository.findTopicIdsByLessonId(lessonId))
                .thenReturn(Set.of("topic-1", "topic-2"));

        assertThatThrownBy(() -> lessonGroupPlanService.createLessonGroupPlan(
                input, lessonGroupId, topicIds, List.of(), List.of()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("topic-rogue");

        verifyNoInteractions(createLessonGroupPlanUseCase, addTopicsToLessonGroupPlanUseCase,
                addMaterialsToLessonGroupPlanUseCase);
    }

    @DisplayName("Should delegate to AddMaterialsToLessonGroupPlanUseCase when materialIds is non-empty")
    @Test
    void createLessonGroupPlan_withMaterials_delegates() {
        UUID lessonGroupUuid = UUID.randomUUID();
        String lessonGroupId = lessonGroupUuid.toString();
        String lessonId = UUID.randomUUID().toString();
        String tenantId = "tenant-123";
        List<String> materialIds = List.of("mat-1", "mat-2");

        LessonGroup group = LessonGroup.builder().id(lessonGroupId).lessonId(lessonId).build();
        LessonGroupPlan input = LessonGroupPlan.builder().plan("My plan").build();
        LessonGroupPlan created = LessonGroupPlan.builder()
                .id("plan-999")
                .lessonGroupId(lessonGroupId)
                .tenantId(tenantId)
                .plan("My plan")
                .build();

        when(lessonGroupRepository.findById(lessonGroupUuid)).thenReturn(group);
        when(createLessonGroupPlanUseCase.createLessonGroupPlan(input, lessonGroupId)).thenReturn(created);

        lessonGroupPlanService.createLessonGroupPlan(
                input, lessonGroupId, List.of(), materialIds, List.of());

        verify(addMaterialsToLessonGroupPlanUseCase).addMaterialsToLessonGroupPlan("plan-999", materialIds, tenantId);
        verifyNoInteractions(topicLessonLinkRepository, addTopicsToLessonGroupPlanUseCase, lessonObjectiveService);
    }

    @DisplayName("Should create each lesson objective with the created plan's id and tenant when provided")
    @Test
    void createLessonGroupPlan_withObjectives_createsEachObjectiveAndItsTopicLinks() {
        UUID lessonGroupUuid = UUID.randomUUID();
        String lessonGroupId = lessonGroupUuid.toString();
        String lessonId = UUID.randomUUID().toString();
        String tenantId = "tenant-123";

        LessonGroup group = LessonGroup.builder().id(lessonGroupId).lessonId(lessonId).build();
        LessonGroupPlan input = LessonGroupPlan.builder().plan("My plan").build();
        LessonGroupPlan created = LessonGroupPlan.builder()
                .id("plan-999")
                .lessonGroupId(lessonGroupId)
                .tenantId(tenantId)
                .plan("My plan")
                .build();

        LessonObjective obj1 = LessonObjective.builder().name("Obj 1").objective("Goal 1").build();
        LessonObjective obj2 = LessonObjective.builder().name("Obj 2").objective("Goal 2").build();
        List<String> obj1Topics = List.of("topic-a");
        List<String> obj2Topics = List.of("topic-b", "topic-c");

        List<LessonGroupPlanService.NewLessonObjective> newObjectives = List.of(
                new LessonGroupPlanService.NewLessonObjective(obj1, obj1Topics),
                new LessonGroupPlanService.NewLessonObjective(obj2, obj2Topics));

        when(lessonGroupRepository.findById(lessonGroupUuid)).thenReturn(group);
        when(createLessonGroupPlanUseCase.createLessonGroupPlan(input, lessonGroupId)).thenReturn(created);

        LessonGroupPlan result = lessonGroupPlanService.createLessonGroupPlan(
                input, lessonGroupId, List.of(), List.of(), newObjectives);

        assertThat(result).isSameAs(created);

        ArgumentCaptor<LessonObjective> objectiveCaptor = ArgumentCaptor.forClass(LessonObjective.class);
        verify(lessonObjectiveService).createLessonObjective(objectiveCaptor.capture(), eq(obj1Topics));
        verify(lessonObjectiveService).createLessonObjective(objectiveCaptor.capture(), eq(obj2Topics));

        List<LessonObjective> captured = objectiveCaptor.getAllValues();
        assertThat(captured).hasSize(2);
        assertThat(captured).allSatisfy(o -> {
            assertThat(o.getLessonGroupPlanId()).isEqualTo("plan-999");
            assertThat(o.getTenantId()).isEqualTo(tenantId);
        });
        assertThat(captured.get(0).getName()).isEqualTo("Obj 1");
        assertThat(captured.get(1).getName()).isEqualTo("Obj 2");
    }

    @DisplayName("Should delegate findById to the lesson group plan repository")
    @Test
    void findById_delegatesToRepository() {
        UUID id = UUID.randomUUID();
        LessonGroupPlan plan = LessonGroupPlan.builder().id(id.toString()).build();
        when(lessonGroupPlanRepository.findById(id)).thenReturn(plan);

        LessonGroupPlan result = lessonGroupPlanService.findById(id);

        assertThat(result).isSameAs(plan);
        verify(lessonGroupPlanRepository).findById(id);
    }

    @DisplayName("Should delegate findByLessonGroupId to the lesson group plan repository")
    @Test
    void findByLessonGroupId_delegatesToRepository() {
        String lessonGroupId = "group-007";
        LessonGroupPlan plan = LessonGroupPlan.builder().id("plan-123").lessonGroupId(lessonGroupId).build();
        when(lessonGroupPlanRepository.findByLessonGroupId(lessonGroupId)).thenReturn(plan);

        LessonGroupPlan result = lessonGroupPlanService.findByLessonGroupId(lessonGroupId);

        assertThat(result).isSameAs(plan);
        verify(lessonGroupPlanRepository).findByLessonGroupId(lessonGroupId);
    }

    @DisplayName("Should delegate findTopicsByLessonGroupPlanIds to the link repository")
    @Test
    void findTopicsByLessonGroupPlanIds_delegatesToLinkRepository() {
        List<String> planIds = List.of("plan-1", "plan-2");
        Map<String, List<Topic>> expected = Map.of(
                "plan-1", List.of(Topic.builder().id("topic-1").build()),
                "plan-2", List.of(Topic.builder().id("topic-2").build()));
        when(lessonGroupPlanTopicLinkRepository.findTopicsByLessonGroupPlanIds(planIds)).thenReturn(expected);

        Map<String, List<Topic>> result = lessonGroupPlanService.findTopicsByLessonGroupPlanIds(planIds);

        assertThat(result).isSameAs(expected);
        verify(lessonGroupPlanTopicLinkRepository).findTopicsByLessonGroupPlanIds(planIds);
    }
}
