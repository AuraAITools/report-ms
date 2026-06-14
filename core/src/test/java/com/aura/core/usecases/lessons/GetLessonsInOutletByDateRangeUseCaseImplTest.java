package com.aura.core.usecases.lessons;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.aura.core.entities.lessons.Lesson;
import com.aura.core.repositories.LessonRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetLessonsInOutletByDateRangeUseCaseImpl returns calendar-bounded lessons for an outlet")
class GetLessonsInOutletByDateRangeUseCaseImplTest {

    private static final String TENANT = "tenant-1";

    @Mock
    private LessonRepository lessonRepository;

    @InjectMocks
    private GetLessonsInOutletByDateRangeUseCaseImpl useCase;

    @Test
    @DisplayName("Should delegate to repository and return its result for a valid range")
    void shouldDelegateAndReturnRepositoryResult() {
        String outletId = "outlet-1";
        Instant start = Instant.parse("2026-09-01T00:00:00Z");
        Instant end = Instant.parse("2026-09-08T00:00:00Z");
        Lesson lesson = Lesson.builder().id(java.util.UUID.randomUUID().toString()).outletId(outletId).build();
        when(lessonRepository.findAllByOutletIdAndTimeRange(eq(TENANT), eq(outletId), eq(start), eq(end)))
                .thenReturn(List.of(lesson));

        List<Lesson> result = useCase.getLessonsInOutletByDateRange(TENANT, outletId, start, end);

        assertThat(result).containsExactly(lesson);
        verify(lessonRepository).findAllByOutletIdAndTimeRange(TENANT, outletId, start, end);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when endTimestamp equals startTimestamp")
    void shouldRejectZeroLengthRange() {
        Instant t = Instant.parse("2026-09-01T00:00:00Z");
        assertThatThrownBy(() -> useCase.getLessonsInOutletByDateRange(TENANT, "outlet-1", t, t))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("endTimestamp");
        verifyNoInteractions(lessonRepository);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when endTimestamp precedes startTimestamp")
    void shouldRejectInvertedRange() {
        Instant start = Instant.parse("2026-09-08T00:00:00Z");
        Instant end = Instant.parse("2026-09-01T00:00:00Z");
        assertThatThrownBy(() -> useCase.getLessonsInOutletByDateRange(TENANT, "outlet-1", start, end))
                .isInstanceOf(IllegalArgumentException.class);
        verifyNoInteractions(lessonRepository);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when range exceeds 92 days")
    void shouldRejectRangeLongerThan92Days() {
        Instant start = Instant.parse("2026-09-01T00:00:00Z");
        Instant end = start.plus(93, ChronoUnit.DAYS);
        assertThatThrownBy(() -> useCase.getLessonsInOutletByDateRange(TENANT, "outlet-1", start, end))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("92");
        verifyNoInteractions(lessonRepository);
    }

    @Test
    @DisplayName("Should accept a range of exactly 92 days")
    void shouldAcceptRangeAtExactly92Days() {
        Instant start = Instant.parse("2026-09-01T00:00:00Z");
        Instant end = start.plus(92, ChronoUnit.DAYS);
        when(lessonRepository.findAllByOutletIdAndTimeRange(eq(TENANT), eq("outlet-1"), eq(start), eq(end)))
                .thenReturn(List.of());

        List<Lesson> result = useCase.getLessonsInOutletByDateRange(TENANT, "outlet-1", start, end);

        assertThat(result).isEmpty();
        verify(lessonRepository).findAllByOutletIdAndTimeRange(TENANT, "outlet-1", start, end);
    }
}
