package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.lessons.LessonTopicProficiency;
import com.aura.core.repositories.LessonTopicProficiencyRepository;
import com.aura.details.springdata.mappers.lessons.LessonTopicProficiencyMapper;
import com.aura.details.springdata.models.lessons.LessonTopicProficiencyModel;
import com.aura.details.springdata.models.links.StudentLessonReportModel;
import com.aura.details.springdata.models.topics.TopicModel;
import com.aura.details.springdata.repositories.jpa.lessons.JpaLessonTopicProficiencyRepository;
import jakarta.persistence.EntityManager;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class LessonTopicProficiencyRepositoryImpl implements LessonTopicProficiencyRepository {

    private final JpaLessonTopicProficiencyRepository jpaLessonTopicProficiencyRepository;
    private final EntityManager entityManager;

    @Override
    public LessonTopicProficiency save(LessonTopicProficiency proficiency) {
        LessonTopicProficiencyModel model = LessonTopicProficiencyMapper.INSTANCE.toModelEntity(proficiency);
        model.setStudentLessonReport(
                Refs.ref(entityManager, StudentLessonReportModel.class, proficiency.getStudentLessonReportId()));
        if (proficiency.getTopicId() != null) {
            model.setTopic(Refs.ref(entityManager, TopicModel.class, proficiency.getTopicId()));
        }
        LessonTopicProficiencyModel saved = jpaLessonTopicProficiencyRepository.save(model);
        return LessonTopicProficiencyMapper.INSTANCE.toDomainEntity(saved);
    }

    @Override
    public @Nullable LessonTopicProficiency findById(UUID id) {
        return jpaLessonTopicProficiencyRepository.findById(id)
                .map(LessonTopicProficiencyMapper.INSTANCE::toDomainEntity)
                .orElse(null);
    }

    @Override
    public void deleteById(UUID id) {
        jpaLessonTopicProficiencyRepository.deleteById(id);
    }
}
