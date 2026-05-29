package com.aura.details.services;

import com.aura.core.entities.lessons.LessonObjective;
import com.aura.core.entities.lessons.LessonPlan;
import com.aura.core.entities.topics.Topic;
import com.aura.core.repositories.LessonPlanRepository;
import com.aura.core.repositories.LessonPlanTopicLinkRepository;
import com.aura.core.repositories.TopicLessonLinkRepository;
import com.aura.core.usecases.lesson_plans.AddMaterialsToLessonPlanUseCase;
import com.aura.core.usecases.lesson_plans.AddStudentsToLessonPlanUseCase;
import com.aura.core.usecases.lesson_plans.AddTopicsToLessonPlanUseCase;
import com.aura.core.usecases.lesson_plans.CreateLessonPlanUseCase;
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

@DisplayName("LessonPlanService")
@ExtendWith(MockitoExtension.class)
class LessonPlanServiceTest {

    @Mock
    private CreateLessonPlanUseCase createLessonPlanUseCase;
    @Mock
    private AddTopicsToLessonPlanUseCase addTopicsToLessonPlanUseCase;
    @Mock
    private AddMaterialsToLessonPlanUseCase addMaterialsToLessonPlanUseCase;
    @Mock
    private AddStudentsToLessonPlanUseCase addStudentsToLessonPlanUseCase;
    @Mock
    private LessonPlanRepository lessonPlanRepository;
    @Mock
    private LessonPlanTopicLinkRepository lessonPlanTopicLinkRepository;
    @Mock
    private TopicLessonLinkRepository topicLessonLinkRepository;
    @Mock
    private LessonObjectiveService lessonObjectiveService;
    @Mock
    private StudentLessonReportService studentLessonReportService;

    @InjectMocks
    private LessonPlanService lessonPlanService;

    @DisplayName("Should delegate to both use cases and return the created plan when topicIds is non-empty")
    @Test
    void createLessonPlan_withTopics_delegatesToBothUseCases() {
        String lessonId = "lesson-001";
        String tenantId = "tenant-123";
        List<String> topicIds = List.of("topic-1", "topic-2");

        LessonPlan input = LessonPlan.builder().plan("My plan").build();
        LessonPlan created = LessonPlan.builder()
                .id("plan-999")
                .lessonId(lessonId)
                .tenantId(tenantId)
                .plan("My plan")
                .build();

        when(topicLessonLinkRepository.findTopicIdsByLessonId(lessonId))
                .thenReturn(Set.of("topic-1", "topic-2", "topic-3"));
        when(createLessonPlanUseCase.createLessonPlan(input, lessonId)).thenReturn(created);
        when(addTopicsToLessonPlanUseCase.addTopicsToLessonPlan("plan-999", topicIds, tenantId))
                .thenReturn(List.of());

        LessonPlan result = lessonPlanService.createLessonPlan(
                input, lessonId, topicIds, List.of(), List.of(), List.of());

        assertThat(result).isSameAs(created);
        verify(createLessonPlanUseCase).createLessonPlan(input, lessonId);
        verify(addTopicsToLessonPlanUseCase).addTopicsToLessonPlan("plan-999", topicIds, tenantId);
        verifyNoInteractions(addMaterialsToLessonPlanUseCase, addStudentsToLessonPlanUseCase,
                lessonObjectiveService, studentLessonReportService);
    }

    @DisplayName("Should skip addTopicsToLessonPlanUseCase when topicIds is empty")
    @Test
    void createLessonPlan_withEmptyTopics_skipsAddTopicsUseCase() {
        String lessonId = "lesson-001";
        String tenantId = "tenant-123";

        LessonPlan input = LessonPlan.builder().plan("My plan").build();
        LessonPlan created = LessonPlan.builder()
                .id("plan-999")
                .lessonId(lessonId)
                .tenantId(tenantId)
                .plan("My plan")
                .build();

        when(createLessonPlanUseCase.createLessonPlan(input, lessonId)).thenReturn(created);

        LessonPlan result = lessonPlanService.createLessonPlan(
                input, lessonId, List.of(), List.of(), List.of(), List.of());

        assertThat(result).isSameAs(created);
        verify(createLessonPlanUseCase).createLessonPlan(input, lessonId);
        verify(addTopicsToLessonPlanUseCase, never())
                .addTopicsToLessonPlan(eq("plan-999"), eq(List.of()), eq(tenantId));
        verifyNoInteractions(addTopicsToLessonPlanUseCase, addMaterialsToLessonPlanUseCase,
                addStudentsToLessonPlanUseCase, lessonObjectiveService, topicLessonLinkRepository,
                studentLessonReportService);
    }

    @DisplayName("Should reject creation when any topicId is not linked to the lesson")
    @Test
    void createLessonPlan_withTopicNotInLesson_throws() {
        String lessonId = "lesson-001";
        List<String> topicIds = List.of("topic-1", "topic-rogue");
        LessonPlan input = LessonPlan.builder().plan("My plan").build();

        when(topicLessonLinkRepository.findTopicIdsByLessonId(lessonId))
                .thenReturn(Set.of("topic-1", "topic-2"));

        assertThatThrownBy(() -> lessonPlanService.createLessonPlan(
                input, lessonId, topicIds, List.of(), List.of(), List.of()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("topic-rogue");

        verifyNoInteractions(createLessonPlanUseCase, addTopicsToLessonPlanUseCase,
                addMaterialsToLessonPlanUseCase, addStudentsToLessonPlanUseCase,
                studentLessonReportService);
    }

    @DisplayName("Should delegate to AddMaterialsToLessonPlanUseCase when materialIds is non-empty")
    @Test
    void createLessonPlan_withMaterials_delegates() {
        String lessonId = "lesson-001";
        String tenantId = "tenant-123";
        List<String> materialIds = List.of("mat-1", "mat-2");

        LessonPlan input = LessonPlan.builder().plan("My plan").build();
        LessonPlan created = LessonPlan.builder()
                .id("plan-999")
                .lessonId(lessonId)
                .tenantId(tenantId)
                .plan("My plan")
                .build();

        when(createLessonPlanUseCase.createLessonPlan(input, lessonId)).thenReturn(created);

        lessonPlanService.createLessonPlan(
                input, lessonId, List.of(), List.of(), materialIds, List.of());

        verify(addMaterialsToLessonPlanUseCase).addMaterialsToLessonPlan("plan-999", materialIds, tenantId);
        verifyNoInteractions(addStudentsToLessonPlanUseCase, topicLessonLinkRepository,
                addTopicsToLessonPlanUseCase, studentLessonReportService);
    }

    @DisplayName("Should delegate to AddStudentsToLessonPlanUseCase when studentIds is non-empty")
    @Test
    void createLessonPlan_withStudents_delegates() {
        String lessonId = "lesson-001";
        String tenantId = "tenant-123";
        List<String> studentIds = List.of("stu-1", "stu-2");

        LessonPlan input = LessonPlan.builder().plan("My plan").build();
        LessonPlan created = LessonPlan.builder()
                .id("plan-999")
                .lessonId(lessonId)
                .tenantId(tenantId)
                .plan("My plan")
                .build();

        when(createLessonPlanUseCase.createLessonPlan(input, lessonId)).thenReturn(created);

        lessonPlanService.createLessonPlan(
                input, lessonId, List.of(), studentIds, List.of(), List.of());

        verify(addStudentsToLessonPlanUseCase).addStudentsToLessonPlan("plan-999", studentIds, tenantId);
        verifyNoInteractions(addMaterialsToLessonPlanUseCase, topicLessonLinkRepository,
                addTopicsToLessonPlanUseCase);
    }

    @DisplayName("Should create student lesson reports for the plan's students with the plan's topics")
    @Test
    void createLessonPlan_withStudents_createsStudentLessonReports() {
        String lessonId = "lesson-001";
        String tenantId = "tenant-123";
        List<String> topicIds = List.of("topic-1", "topic-2");
        List<String> studentIds = List.of("stu-1", "stu-2");

        LessonPlan input = LessonPlan.builder().plan("My plan").build();
        LessonPlan created = LessonPlan.builder()
                .id("plan-999")
                .lessonId(lessonId)
                .tenantId(tenantId)
                .plan("My plan")
                .build();

        when(topicLessonLinkRepository.findTopicIdsByLessonId(lessonId))
                .thenReturn(Set.of("topic-1", "topic-2"));
        when(createLessonPlanUseCase.createLessonPlan(input, lessonId)).thenReturn(created);
        when(addTopicsToLessonPlanUseCase.addTopicsToLessonPlan("plan-999", topicIds, tenantId))
                .thenReturn(List.of());

        lessonPlanService.createLessonPlan(
                input, lessonId, topicIds, studentIds, List.of(), List.of());

        verify(studentLessonReportService).createReportsForStudentsInPlan(
                lessonId, studentIds, topicIds, tenantId);
    }

    @DisplayName("Should skip student lesson reports when studentIds is empty")
    @Test
    void createLessonPlan_withoutStudents_skipsStudentLessonReports() {
        String lessonId = "lesson-001";
        String tenantId = "tenant-123";

        LessonPlan input = LessonPlan.builder().plan("My plan").build();
        LessonPlan created = LessonPlan.builder()
                .id("plan-999")
                .lessonId(lessonId)
                .tenantId(tenantId)
                .plan("My plan")
                .build();

        when(createLessonPlanUseCase.createLessonPlan(input, lessonId)).thenReturn(created);

        lessonPlanService.createLessonPlan(
                input, lessonId, List.of(), List.of(), List.of(), List.of());

        verifyNoInteractions(studentLessonReportService);
    }

    @DisplayName("Should create each lesson objective with the created plan's id and tenant when provided")
    @Test
    void createLessonPlan_withObjectives_createsEachObjectiveAndItsTopicLinks() {
        String lessonId = "lesson-001";
        String tenantId = "tenant-123";

        LessonPlan input = LessonPlan.builder().plan("My plan").build();
        LessonPlan created = LessonPlan.builder()
                .id("plan-999")
                .lessonId(lessonId)
                .tenantId(tenantId)
                .plan("My plan")
                .build();

        LessonObjective obj1 = LessonObjective.builder().name("Obj 1").objective("Goal 1").build();
        LessonObjective obj2 = LessonObjective.builder().name("Obj 2").objective("Goal 2").build();
        List<String> obj1Topics = List.of("topic-a");
        List<String> obj2Topics = List.of("topic-b", "topic-c");

        List<LessonPlanService.NewLessonObjective> newObjectives = List.of(
                new LessonPlanService.NewLessonObjective(obj1, obj1Topics),
                new LessonPlanService.NewLessonObjective(obj2, obj2Topics));

        when(createLessonPlanUseCase.createLessonPlan(input, lessonId)).thenReturn(created);

        LessonPlan result = lessonPlanService.createLessonPlan(
                input, lessonId, List.of(), List.of(), List.of(), newObjectives);

        assertThat(result).isSameAs(created);

        ArgumentCaptor<LessonObjective> objectiveCaptor = ArgumentCaptor.forClass(LessonObjective.class);
        verify(lessonObjectiveService).createLessonObjective(objectiveCaptor.capture(), eq(obj1Topics));
        verify(lessonObjectiveService).createLessonObjective(objectiveCaptor.capture(), eq(obj2Topics));

        List<LessonObjective> captured = objectiveCaptor.getAllValues();
        assertThat(captured).hasSize(2);
        assertThat(captured).allSatisfy(o -> {
            assertThat(o.getLessonPlanId()).isEqualTo("plan-999");
            assertThat(o.getTenantId()).isEqualTo(tenantId);
        });
        assertThat(captured.get(0).getName()).isEqualTo("Obj 1");
        assertThat(captured.get(1).getName()).isEqualTo("Obj 2");
    }

    @DisplayName("Should delegate findById to the lesson plan repository")
    @Test
    void findById_delegatesToRepository() {
        UUID id = UUID.randomUUID();
        LessonPlan plan = LessonPlan.builder().id(id.toString()).build();
        when(lessonPlanRepository.findById(id)).thenReturn(plan);

        LessonPlan result = lessonPlanService.findById(id);

        assertThat(result).isSameAs(plan);
        verify(lessonPlanRepository).findById(id);
    }

    @DisplayName("Should delegate findAllByLessonId to the lesson plan repository")
    @Test
    void findAllByLessonId_delegatesToRepository() {
        String lessonId = "lesson-007";
        LessonPlan plan1 = LessonPlan.builder().id("plan-123").lessonId(lessonId).build();
        LessonPlan plan2 = LessonPlan.builder().id("plan-456").lessonId(lessonId).build();
        when(lessonPlanRepository.findAllByLessonId(lessonId)).thenReturn(List.of(plan1, plan2));

        List<LessonPlan> result = lessonPlanService.findAllByLessonId(lessonId);

        assertThat(result).containsExactly(plan1, plan2);
        verify(lessonPlanRepository).findAllByLessonId(lessonId);
    }

    @DisplayName("Should delegate findTopicsByLessonPlanIds to the link repository")
    @Test
    void findTopicsByLessonPlanIds_delegatesToLinkRepository() {
        List<String> planIds = List.of("plan-1", "plan-2");
        Map<String, List<Topic>> expected = Map.of(
                "plan-1", List.of(Topic.builder().id("topic-1").build()),
                "plan-2", List.of(Topic.builder().id("topic-2").build()));
        when(lessonPlanTopicLinkRepository.findTopicsByLessonPlanIds(planIds)).thenReturn(expected);

        Map<String, List<Topic>> result = lessonPlanService.findTopicsByLessonPlanIds(planIds);

        assertThat(result).isSameAs(expected);
        verify(lessonPlanTopicLinkRepository).findTopicsByLessonPlanIds(planIds);
    }
}
