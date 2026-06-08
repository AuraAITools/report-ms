package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.lessons.LessonHomeworkCompletion;
import com.aura.core.repositories.LessonHomeworkCompletionRepository;
import com.aura.details.springdata.mappers.lessons.LessonHomeworkCompletionMapper;
import com.aura.details.springdata.models.lessons.LessonHomeworkCompletionModel;
import com.aura.details.springdata.models.links.StudentLessonReportModel;
import com.aura.details.springdata.repositories.jpa.lessons.JpaLessonHomeworkCompletionRepository;
import jakarta.persistence.EntityManager;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class LessonHomeworkCompletionRepositoryImpl implements LessonHomeworkCompletionRepository {

    private final JpaLessonHomeworkCompletionRepository jpaLessonHomeworkCompletionRepository;
    private final EntityManager entityManager;

    @Override
    public LessonHomeworkCompletion save(LessonHomeworkCompletion completion) {
        LessonHomeworkCompletionModel model = LessonHomeworkCompletionMapper.INSTANCE.toModelEntity(completion);
        model.setStudentLessonReport(
                Refs.ref(entityManager, StudentLessonReportModel.class, completion.getStudentLessonReportId()));
        LessonHomeworkCompletionModel saved = jpaLessonHomeworkCompletionRepository.save(model);
        return LessonHomeworkCompletionMapper.INSTANCE.toDomainEntity(saved);
    }

    @Override
    public @Nullable LessonHomeworkCompletion findById(UUID id) {
        return jpaLessonHomeworkCompletionRepository.findById(id)
                .map(LessonHomeworkCompletionMapper.INSTANCE::toDomainEntity)
                .orElse(null);
    }

    @Override
    public void deleteById(UUID id) {
        jpaLessonHomeworkCompletionRepository.deleteById(id);
    }
}
