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
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class LessonPlanService {

    private final CreateLessonPlanUseCase createLessonPlanUseCase;
    private final AddTopicsToLessonPlanUseCase addTopicsToLessonPlanUseCase;
    private final AddMaterialsToLessonPlanUseCase addMaterialsToLessonPlanUseCase;
    private final AddStudentsToLessonPlanUseCase addStudentsToLessonPlanUseCase;
    private final LessonPlanRepository lessonPlanRepository;
    private final LessonPlanTopicLinkRepository lessonPlanTopicLinkRepository;
    private final TopicLessonLinkRepository topicLessonLinkRepository;
    private final LessonObjectiveService lessonObjectiveService;
    private final StudentLessonReportService studentLessonReportService;

    @Transactional
    public LessonPlan createLessonPlan(LessonPlan plan, String lessonId, List<String> topicIds,
                                       List<String> studentIds, List<String> materialIds,
                                       List<NewLessonObjective> lessonObjectives) {
        if (!topicIds.isEmpty()) {
            Set<String> lessonTopicIds = topicLessonLinkRepository.findTopicIdsByLessonId(lessonId);
            List<String> missingTopics = topicIds.stream()
                    .filter(t -> !lessonTopicIds.contains(t))
                    .toList();
            if (!missingTopics.isEmpty()) {
                throw new RuntimeException("topics not in lesson: " + missingTopics);
            }
        }

        LessonPlan created = createLessonPlanUseCase.createLessonPlan(plan, lessonId);
        if (!topicIds.isEmpty()) {
            addTopicsToLessonPlanUseCase.addTopicsToLessonPlan(created.getId(), topicIds, created.getTenantId());
        }
        if (!materialIds.isEmpty()) {
            addMaterialsToLessonPlanUseCase.addMaterialsToLessonPlan(created.getId(), materialIds, created.getTenantId());
        }
        if (!studentIds.isEmpty()) {
            addStudentsToLessonPlanUseCase.addStudentsToLessonPlan(created.getId(), studentIds, created.getTenantId());
        }
        for (NewLessonObjective newObjective : lessonObjectives) {
            LessonObjective objective = newObjective.objective();
            objective.setLessonPlanId(created.getId());
            objective.setTenantId(created.getTenantId());
            lessonObjectiveService.createLessonObjective(objective, newObjective.topicIds());
        }
        if (!studentIds.isEmpty()) {
            studentLessonReportService.createReportsForStudentsInPlan(
                    lessonId, studentIds, topicIds, created.getTenantId());
        }
        return created;
    }

    public @Nullable LessonPlan findById(UUID id) {
        return lessonPlanRepository.findById(id);
    }

    public List<LessonPlan> findAllByLessonId(String lessonId) {
        return lessonPlanRepository.findAllByLessonId(lessonId);
    }

    public Map<String, List<Topic>> findTopicsByLessonPlanIds(List<String> ids) {
        return lessonPlanTopicLinkRepository.findTopicsByLessonPlanIds(ids);
    }

    public record NewLessonObjective(LessonObjective objective, List<String> topicIds) {
    }
}
