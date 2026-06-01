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
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public class LessonGroupPlanService {

    private final CreateLessonGroupPlanUseCase createLessonGroupPlanUseCase;
    private final AddTopicsToLessonGroupPlanUseCase addTopicsToLessonGroupPlanUseCase;
    private final AddMaterialsToLessonGroupPlanUseCase addMaterialsToLessonGroupPlanUseCase;
    private final LessonGroupPlanRepository lessonGroupPlanRepository;
    private final LessonGroupPlanTopicLinkRepository lessonGroupPlanTopicLinkRepository;
    private final TopicLessonLinkRepository topicLessonLinkRepository;
    private final LessonGroupRepository lessonGroupRepository;
    private final LessonObjectiveService lessonObjectiveService;

    @Transactional
    public LessonGroupPlan createLessonGroupPlan(LessonGroupPlan plan, String lessonGroupId, List<String> topicIds,
                                                  List<String> materialIds,
                                                  List<NewLessonObjective> lessonObjectives) {
        LessonGroup group = ofNullable(lessonGroupRepository.findById(UUID.fromString(lessonGroupId)))
                .orElseThrow(() -> new RuntimeException("lesson group not found"));
        String lessonId = group.getLessonId();

        if (!topicIds.isEmpty()) {
            Set<String> lessonTopicIds = topicLessonLinkRepository.findTopicIdsByLessonId(lessonId);
            List<String> missingTopics = topicIds.stream()
                    .filter(t -> !lessonTopicIds.contains(t))
                    .toList();
            if (!missingTopics.isEmpty()) {
                throw new RuntimeException("topics not in lesson: " + missingTopics);
            }
        }

        LessonGroupPlan created = createLessonGroupPlanUseCase.createLessonGroupPlan(plan, lessonGroupId);
        if (!topicIds.isEmpty()) {
            addTopicsToLessonGroupPlanUseCase.addTopicsToLessonGroupPlan(created.getId(), topicIds, created.getTenantId());
        }
        if (!materialIds.isEmpty()) {
            addMaterialsToLessonGroupPlanUseCase.addMaterialsToLessonGroupPlan(created.getId(), materialIds, created.getTenantId());
        }
        for (NewLessonObjective newObjective : lessonObjectives) {
            LessonObjective objective = newObjective.objective();
            objective.setLessonGroupPlanId(created.getId());
            objective.setTenantId(created.getTenantId());
            lessonObjectiveService.createLessonObjective(objective, newObjective.topicIds());
        }
        return created;
    }

    public @Nullable LessonGroupPlan findById(UUID id) {
        return lessonGroupPlanRepository.findById(id);
    }

    public @Nullable LessonGroupPlan findByLessonGroupId(String lessonGroupId) {
        return lessonGroupPlanRepository.findByLessonGroupId(lessonGroupId);
    }

    public Map<String, List<Topic>> findTopicsByLessonGroupPlanIds(List<String> ids) {
        return lessonGroupPlanTopicLinkRepository.findTopicsByLessonGroupPlanIds(ids);
    }

    public record NewLessonObjective(LessonObjective objective, List<String> topicIds) {
    }
}
