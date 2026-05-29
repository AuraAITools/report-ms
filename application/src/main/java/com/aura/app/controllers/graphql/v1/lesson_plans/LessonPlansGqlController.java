package com.aura.app.controllers.graphql.v1.lesson_plans;

import com.aura.app.codegen.types.CreateLessonPlanInLessonInput;
import com.aura.app.codegen.types.LessonPlanResponse;
import com.aura.app.codegen.types.LessonResponse;
import com.aura.app.codegen.types.TopicResponse;
import com.aura.app.mappers.LessonMapper;
import com.aura.app.mappers.LessonPlanMapper;
import com.aura.app.mappers.TopicMapper;
import com.aura.core.auth.User;
import com.aura.core.entities.lessons.Lesson;
import com.aura.core.entities.lessons.LessonPlan;
import com.aura.core.entities.topics.Topic;
import com.aura.core.repositories.LessonPlanRepository;
import com.aura.core.repositories.LessonRepository;
import com.aura.details.services.LessonPlanService;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
public class LessonPlansGqlController {

    private final LessonPlanService lessonPlanService;
    private final LessonPlanRepository lessonPlanRepository;
    private final LessonRepository lessonRepository;

    @PreAuthorize("hasPermission(#input.lessonId, 'lesson', 'create_lesson_plan')")
    @MutationMapping
    public LessonPlanResponse createLessonPlanInLesson(
            @Argument("input") CreateLessonPlanInLessonInput input,
            @ContextValue(name = "user") User user) {
        LessonPlan toCreate = LessonPlanMapper.INSTANCE.toDomainEntity(input);
        List<LessonPlanService.NewLessonObjective> newObjectives = input.getLessonObjectives().stream()
                .map(o -> new LessonPlanService.NewLessonObjective(
                        LessonPlanMapper.INSTANCE.toDomainEntity(o), o.getTopicIds()))
                .toList();
        LessonPlan created = lessonPlanService.createLessonPlan(
                toCreate, input.getLessonId(), input.getTopicIds(),
                input.getStudentIds(), input.getMaterialIds(), newObjectives);
        return LessonPlanMapper.INSTANCE.toDto(created);
    }

    @BatchMapping(typeName = "LessonPlanResponse", field = "lesson")
    public Map<LessonPlanResponse, LessonResponse> lesson(List<LessonPlanResponse> responses) {
        List<UUID> planIds = responses.stream().map(r -> UUID.fromString(r.getId())).toList();

        Map<String, LessonPlan> planById = lessonPlanRepository.findAllByIds(planIds).stream()
                .collect(Collectors.toMap(LessonPlan::getId, Function.identity()));

        Set<String> lessonIds = planById.values().stream()
                .map(LessonPlan::getLessonId)
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toSet());

        Map<String, LessonResponse> lessonById = lessonRepository.findAllById(
                        lessonIds.stream().map(UUID::fromString).toList()).stream()
                .collect(Collectors.toMap(Lesson::getId, LessonMapper.INSTANCE::toResponseDto));

        return responses.stream()
                .filter(r -> {
                    LessonPlan p = planById.get(r.getId());
                    return p != null && p.getLessonId() != null && lessonById.containsKey(p.getLessonId());
                })
                .collect(Collectors.toMap(
                        Function.identity(),
                        r -> lessonById.get(planById.get(r.getId()).getLessonId())));
    }

    @BatchMapping(typeName = "LessonPlanResponse", field = "topics")
    public Map<LessonPlanResponse, List<TopicResponse>> topics(List<LessonPlanResponse> responses) {
        List<String> planIds = responses.stream().map(LessonPlanResponse::getId).toList();
        Map<String, List<Topic>> byPlan = lessonPlanService.findTopicsByLessonPlanIds(planIds);
        return responses.stream().collect(Collectors.toMap(
                Function.identity(),
                r -> byPlan.getOrDefault(r.getId(), List.of()).stream()
                        .map(TopicMapper.INSTANCE::toDto).toList()));
    }
}
