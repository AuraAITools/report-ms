package com.aura.details.services;

import com.aura.core.entities.lessons.LessonParticipationReview;
import com.aura.core.usecases.lesson_participation_reviews.CreateLessonParticipationReviewUseCase;
import com.aura.core.usecases.lesson_participation_reviews.DeleteLessonParticipationReviewUseCase;
import com.aura.core.usecases.lesson_participation_reviews.LessonParticipationReviewQueryUseCase;
import com.aura.core.usecases.lesson_participation_reviews.UpdateLessonParticipationReviewUseCase;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class LessonParticipationReviewService {

    private final CreateLessonParticipationReviewUseCase createUseCase;
    private final LessonParticipationReviewQueryUseCase queryUseCase;
    private final UpdateLessonParticipationReviewUseCase updateUseCase;
    private final DeleteLessonParticipationReviewUseCase deleteUseCase;

    public @Nullable LessonParticipationReview findById(UUID id) {
        return queryUseCase.findById(id);
    }

    @Transactional
    public LessonParticipationReview create(String studentLessonReportId,
                                            LessonParticipationReview.PARTICIPATION_TYPE participationType,
                                            String tenantId) {
        return createUseCase.create(studentLessonReportId, participationType, tenantId);
    }

    @Transactional
    public @Nullable LessonParticipationReview update(String id, LessonParticipationReview.PARTICIPATION_TYPE participationType) {
        return updateUseCase.update(id, participationType);
    }

    @Transactional
    public void deleteById(UUID id) {
        deleteUseCase.deleteById(id);
    }
}
