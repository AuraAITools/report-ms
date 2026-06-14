package com.aura.core.usecases.lessons;

import com.aura.core.entities.lessons.Lesson;
import com.aura.core.repositories.LessonRepository;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetLessonsInOutletByDateRangeUseCaseImpl implements GetLessonsInOutletByDateRangeUseCase {

    private static final Duration MAX_RANGE = Duration.ofDays(92);

    private final LessonRepository lessonRepository;

    @Override
    public List<Lesson> getLessonsInOutletByDateRange(String tenantId,
                                                      String outletId,
                                                      Instant startInclusive,
                                                      Instant endExclusive) {
        if (!endExclusive.isAfter(startInclusive)) {
            throw new IllegalArgumentException(
                    "endTimestamp must be strictly after startTimestamp");
        }
        Duration range = Duration.between(startInclusive, endExclusive);
        if (range.compareTo(MAX_RANGE) > 0) {
            throw new IllegalArgumentException(
                    "Date range must not exceed 92 days (got " + range.toDays() + " days)");
        }
        return lessonRepository.findAllByOutletIdAndTimeRange(
                tenantId, outletId, startInclusive, endExclusive);
    }
}
