package com.aura.app.controllers.graphql.v1.lesson_group_plans;

import com.aura.app.codegen.types.CreateLessonGroupPlanInLessonGroupInput;
import com.aura.app.codegen.types.LessonGroupPlanResponse;
import com.aura.app.codegen.types.LessonGroupResponse;
import com.aura.app.codegen.types.LessonObjectiveResponse;
import com.aura.app.codegen.types.MaterialResponse;
import com.aura.app.codegen.types.TopicResponse;
import com.aura.app.mappers.LessonGroupMapper;
import com.aura.app.mappers.LessonGroupPlanMapper;
import com.aura.app.mappers.LessonObjectiveMapper;
import com.aura.app.mappers.MaterialMapper;
import com.aura.app.mappers.TopicMapper;
import com.aura.core.auth.User;
import com.aura.core.entities.lessons.LessonGroup;
import com.aura.core.entities.lessons.LessonGroupPlan;
import com.aura.core.entities.lessons.LessonObjective;
import com.aura.core.entities.materials.Material;
import com.aura.core.entities.topics.Topic;
import com.aura.core.repositories.LessonGroupPlanRepository;
import com.aura.core.repositories.LessonGroupRepository;
import com.aura.core.repositories.LessonObjectiveRepository;
import com.aura.core.repositories.MaterialRepository;
import com.aura.details.services.LessonGroupPlanService;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class LessonGroupPlansGqlController {

    private final LessonGroupPlanService lessonGroupPlanService;
    private final LessonGroupPlanRepository lessonGroupPlanRepository;
    private final LessonGroupRepository lessonGroupRepository;
    private final LessonObjectiveRepository lessonObjectiveRepository;
    private final MaterialRepository materialRepository;

    @PreAuthorize("hasPermission(#input.lessonId, 'lesson', 'create_lesson_plan')")
    @MutationMapping
    public LessonGroupPlanResponse createLessonGroupPlanInLessonGroup(
            @Argument("input") CreateLessonGroupPlanInLessonGroupInput input,
            @ContextValue(name = "user") User user) {
        LessonGroupPlan toCreate = LessonGroupPlanMapper.INSTANCE.toDomainEntity(input);
        List<LessonGroupPlanService.NewLessonObjective> newObjectives = input.getLessonObjectives().stream()
                .map(o -> new LessonGroupPlanService.NewLessonObjective(
                        LessonGroupPlanMapper.INSTANCE.toDomainEntity(o), o.getTopicIds()))
                .toList();
        LessonGroupPlan created = lessonGroupPlanService.createLessonGroupPlan(
                toCreate, input.getLessonGroupId(), input.getTopicIds(),
                input.getMaterialIds(), newObjectives);
        return LessonGroupPlanMapper.INSTANCE.toDto(created);
    }

    @BatchMapping(typeName = "LessonGroupPlanResponse", field = "lessonGroup")
    public Map<LessonGroupPlanResponse, LessonGroupResponse> lessonGroup(List<LessonGroupPlanResponse> responses) {
        List<UUID> planIds = responses.stream().map(r -> UUID.fromString(r.getId())).toList();
        Map<String, LessonGroupPlan> planById = lessonGroupPlanRepository.findAllByIds(planIds).stream()
                .collect(Collectors.toMap(LessonGroupPlan::getId, Function.identity()));
        List<UUID> groupIds = planById.values().stream()
                .map(p -> UUID.fromString(p.getLessonGroupId()))
                .filter(java.util.Objects::nonNull)
                .distinct()
                .toList();
        Map<String, LessonGroupResponse> groupById = lessonGroupRepository.findAllByIds(groupIds).stream()
                .collect(Collectors.toMap(LessonGroup::getId, LessonGroupMapper.INSTANCE::toDto));
        return responses.stream()
                .filter(r -> {
                    LessonGroupPlan p = planById.get(r.getId());
                    return p != null && p.getLessonGroupId() != null && groupById.containsKey(p.getLessonGroupId());
                })
                .collect(Collectors.toMap(
                        Function.identity(),
                        r -> groupById.get(planById.get(r.getId()).getLessonGroupId())));
    }

    @BatchMapping(typeName = "LessonGroupPlanResponse", field = "topics")
    public Map<LessonGroupPlanResponse, List<TopicResponse>> topics(List<LessonGroupPlanResponse> responses) {
        List<String> planIds = responses.stream().map(LessonGroupPlanResponse::getId).toList();
        Map<String, List<Topic>> byPlan = lessonGroupPlanService.findTopicsByLessonGroupPlanIds(planIds);
        return responses.stream().collect(Collectors.toMap(
                Function.identity(),
                r -> byPlan.getOrDefault(r.getId(), List.of()).stream()
                        .map(TopicMapper.INSTANCE::toDto).toList()));
    }

    @BatchMapping(typeName = "LessonGroupPlanResponse", field = "lessonObjectives")
    public Map<LessonGroupPlanResponse, List<LessonObjectiveResponse>> lessonObjectives(List<LessonGroupPlanResponse> responses) {
        List<String> planIds = responses.stream().map(LessonGroupPlanResponse::getId).toList();
        Map<String, List<LessonObjective>> byPlan = lessonObjectiveRepository.findAllByLessonGroupPlanIds(planIds);
        return responses.stream().collect(Collectors.toMap(
                Function.identity(),
                r -> byPlan.getOrDefault(r.getId(), List.of()).stream()
                        .map(LessonObjectiveMapper.INSTANCE::toDto).toList()));
    }

    @BatchMapping(typeName = "LessonGroupPlanResponse", field = "materials")
    public Map<LessonGroupPlanResponse, List<MaterialResponse>> materials(List<LessonGroupPlanResponse> responses) {
        List<String> planIds = responses.stream().map(LessonGroupPlanResponse::getId).toList();
        Map<String, List<Material>> byPlan = materialRepository.findAllByLessonGroupPlanIds(planIds);
        return responses.stream().collect(Collectors.toMap(
                Function.identity(),
                r -> byPlan.getOrDefault(r.getId(), List.of()).stream()
                        .map(MaterialMapper.INSTANCE::toDto).toList()));
    }
}
