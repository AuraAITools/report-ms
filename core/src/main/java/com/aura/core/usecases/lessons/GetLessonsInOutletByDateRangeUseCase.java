package com.aura.core.usecases.lessons;

import com.aura.core.entities.lessons.Lesson;
import java.time.Instant;
import java.util.List;

public interface GetLessonsInOutletByDateRangeUseCase {

    /**
     * Returns lessons in the given tenant + outlet whose time window overlaps
     * the half-open interval [startInclusive, endExclusive).
     *
     * @throws IllegalArgumentException if endTimestamp is not strictly after startTimestamp,
     *         or if the range exceeds 92 days.
     */
    List<Lesson> getLessonsInOutletByDateRange(String tenantId,
                                               String outletId,
                                               Instant startInclusive,
                                               Instant endExclusive);
}
