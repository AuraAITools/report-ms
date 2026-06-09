package com.aura.core.usecases.lesson_participation_reviews;

import com.aura.core.entities.lessons.LessonParticipationReview;
import com.aura.core.repositories.LessonParticipationReviewRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("CreateLessonParticipationReviewUseCaseImpl")
@ExtendWith(MockitoExtension.class)
class CreateLessonParticipationReviewUseCaseImplTest {

    @Mock
    private LessonParticipationReviewRepository repository;

    @InjectMocks
    private CreateLessonParticipationReviewUseCaseImpl useCase;

    @DisplayName("Should build a participation review with the given report id, participation type, tenant, and persist it")
    @Test
    void create_persistsBuiltEntity() {
        when(repository.save(any(LessonParticipationReview.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        LessonParticipationReview result = useCase.create(
                "report-1", LessonParticipationReview.PARTICIPATION_TYPE.ATTENTIVE, "tenant-X");

        ArgumentCaptor<LessonParticipationReview> captor = ArgumentCaptor.forClass(LessonParticipationReview.class);
        verify(repository).save(captor.capture());
        LessonParticipationReview saved = captor.getValue();

        assertThat(saved.getStudentLessonReportId()).isEqualTo("report-1");
        assertThat(saved.getParticipationType())
                .isEqualTo(LessonParticipationReview.PARTICIPATION_TYPE.ATTENTIVE);
        assertThat(saved.getTenantId()).isEqualTo("tenant-X");
        assertThat(result).isSameAs(saved);
    }
}
