package com.aura.details.services;

import com.aura.core.entities.lessons.LessonObjective;
import com.aura.core.repositories.LessonObjectiveRepository;
import com.aura.core.usecases.lesson_objectives.AddTopicsToLessonObjectiveUseCase;
import com.aura.core.usecases.lesson_objectives.CreateLessonObjectiveUseCase;
import com.aura.core.usecases.lesson_objectives.DeleteLessonObjectiveUseCase;
import com.aura.core.usecases.lesson_objectives.LessonObjectiveQueryUseCase;
import com.aura.core.usecases.lesson_objectives.UpdateLessonObjectiveUseCase;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class LessonObjectiveService {

    private final CreateLessonObjectiveUseCase createLessonObjectiveUseCase;
    private final UpdateLessonObjectiveUseCase updateLessonObjectiveUseCase;
    private final DeleteLessonObjectiveUseCase deleteLessonObjectiveUseCase;
    private final LessonObjectiveQueryUseCase lessonObjectiveQueryUseCase;
    private final AddTopicsToLessonObjectiveUseCase addTopicsToLessonObjectiveUseCase;
    private final LessonObjectiveRepository lessonObjectiveRepository;

    @Transactional
    public LessonObjective createLessonObjective(LessonObjective lessonObjective, List<String> topicIds) {
        LessonObjective created = createLessonObjectiveUseCase.createLessonObjective(lessonObjective);
        addTopicsToLessonObjectiveUseCase.addTopicsToLessonObjective(
                created.getId(), topicIds, created.getTenantId());
        return created;
    }

    @Transactional
    public @Nullable LessonObjective updateLessonObjective(LessonObjective lessonObjective, List<String> topicIds) {
        LessonObjective updated = updateLessonObjectiveUseCase.updateLessonObjective(lessonObjective);
        if (updated == null) {
            return null;
        }
        lessonObjectiveRepository.deleteAllLessonObjectiveTopicLinksByLessonObjectiveId(updated.getId());
        addTopicsToLessonObjectiveUseCase.addTopicsToLessonObjective(
                updated.getId(), topicIds, updated.getTenantId());
        return updated;
    }

    public @Nullable LessonObjective findById(String id) {
        return lessonObjectiveQueryUseCase.findById(id);
    }

    public List<LessonObjective> findAllByLessonGroupPlanId(String lessonGroupPlanId) {
        return lessonObjectiveQueryUseCase.findAllByLessonGroupPlanId(lessonGroupPlanId);
    }

    @Transactional
    public void deleteById(String id) {
        lessonObjectiveRepository.deleteAllLessonObjectiveTopicLinksByLessonObjectiveId(id);
        deleteLessonObjectiveUseCase.deleteById(id);
    }
}
