package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.lessons.LessonQuiz;
import com.aura.core.repositories.LessonQuizRepository;
import com.aura.details.springdata.mappers.lessons.LessonQuizMapper;
import com.aura.details.springdata.models.lessons.LessonGroupPlanModel;
import com.aura.details.springdata.models.lessons.LessonQuizModel;
import com.aura.details.springdata.repositories.jpa.lessons.JpaLessonQuizRepository;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class LessonQuizRepositoryImpl implements LessonQuizRepository {

    private final JpaLessonQuizRepository jpaLessonQuizRepository;
    private final EntityManager entityManager;

    @Override
    public LessonQuiz save(LessonQuiz lessonQuiz) {
        LessonQuizModel toSave = LessonQuizMapper.INSTANCE.toModelEntity(lessonQuiz);
        toSave.setLessonGroupPlan(Refs.ref(entityManager, LessonGroupPlanModel.class, lessonQuiz.getLessonGroupPlanId()));
        LessonQuizModel saved = jpaLessonQuizRepository.save(toSave);
        return LessonQuizMapper.INSTANCE.toDomainEntity(saved);
    }

    @Override
    public @Nullable LessonQuiz findById(String id) {
        return jpaLessonQuizRepository.findById(UUID.fromString(id))
                .map(LessonQuizMapper.INSTANCE::toDomainEntity)
                .orElse(null);
    }

    @Override
    public List<LessonQuiz> findAllByLessonGroupPlanId(String lessonGroupPlanId) {
        return jpaLessonQuizRepository.findAllByLessonGroupPlanId(UUID.fromString(lessonGroupPlanId)).stream()
                .map(LessonQuizMapper.INSTANCE::toDomainEntity)
                .toList();
    }

    @Override
    public Map<String, List<LessonQuiz>> findAllByLessonGroupPlanIds(List<String> lessonGroupPlanIds) {
        if (lessonGroupPlanIds.isEmpty()) return Collections.emptyMap();
        List<UUID> uuids = lessonGroupPlanIds.stream().map(UUID::fromString).toList();
        Map<String, List<LessonQuiz>> result = new HashMap<>();
        for (Object[] row : jpaLessonQuizRepository.findQuizzesByLessonGroupPlanIds(uuids)) {
            String planId = row[0].toString();
            LessonQuizModel model = (LessonQuizModel) row[1];
            result.computeIfAbsent(planId, k -> new ArrayList<>())
                    .add(LessonQuizMapper.INSTANCE.toDomainEntity(model));
        }
        return result;
    }

    @Override
    public void deleteById(String id) {
        jpaLessonQuizRepository.deleteById(UUID.fromString(id));
    }
}
