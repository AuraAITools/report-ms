package com.aura.core.usecases.lesson_participation_reviews;

import com.aura.core.entities.lessons.LessonParticipationReview;
import com.aura.core.repositories.LessonParticipationReviewRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateLessonParticipationReviewUseCaseImpl implements CreateLessonParticipationReviewUseCase {
    private final LessonParticipationReviewRepository lessonParticipationReviewRepository;

    @Override
    public LessonParticipationReview create(String studentLessonReportId,
                                            LessonParticipationReview.PARTICIPATION_TYPE participationType,
                                            String tenantId) {
        LessonParticipationReview entity = LessonParticipationReview.builder()
                .tenantId(tenantId)
                .studentLessonReportId(studentLessonReportId)
                .participationType(participationType)
                .build();
        return lessonParticipationReviewRepository.save(entity);
    }
}
