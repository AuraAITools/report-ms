package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.lessons.LessonParticipationReview;
import com.aura.core.repositories.LessonParticipationReviewRepository;
import com.aura.details.springdata.mappers.lessons.LessonParticipationReviewMapper;
import com.aura.details.springdata.models.lessons.LessonParticipationReviewModel;
import com.aura.details.springdata.models.links.StudentLessonReportModel;
import com.aura.details.springdata.repositories.jpa.lessons.JpaLessonParticipationReviewRepository;
import jakarta.persistence.EntityManager;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class LessonParticipationReviewRepositoryImpl implements LessonParticipationReviewRepository {

    private final JpaLessonParticipationReviewRepository jpaLessonParticipationReviewRepository;
    private final EntityManager entityManager;

    @Override
    public LessonParticipationReview save(LessonParticipationReview review) {
        LessonParticipationReviewModel model = LessonParticipationReviewMapper.INSTANCE.toModelEntity(review);
        model.setStudentLessonReport(
                Refs.ref(entityManager, StudentLessonReportModel.class, review.getStudentLessonReportId()));
        LessonParticipationReviewModel saved = jpaLessonParticipationReviewRepository.save(model);
        return LessonParticipationReviewMapper.INSTANCE.toDomainEntity(saved);
    }

    @Override
    public @Nullable LessonParticipationReview findById(UUID id) {
        return jpaLessonParticipationReviewRepository.findById(id)
                .map(LessonParticipationReviewMapper.INSTANCE::toDomainEntity)
                .orElse(null);
    }

    @Override
    public void deleteById(UUID id) {
        jpaLessonParticipationReviewRepository.deleteById(id);
    }
}
