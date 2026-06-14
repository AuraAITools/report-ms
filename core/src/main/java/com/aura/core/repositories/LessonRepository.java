package com.aura.core.repositories;

import com.aura.core.entities.lessons.Lesson;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public interface LessonRepository {
    Lesson save(Lesson lesson);

    Lesson saveAndFlush(Lesson lesson);

    List<Lesson> updateLessons(List<Lesson> lessons);

    @Nullable Lesson findById(UUID id);

    List<Lesson> findAllById(List<UUID> ids);

    Map<String, List<Lesson>> findAllByCourseIds(List<String> courseIds);

    List<Lesson> findAllByCourseId(String courseId);

    List<Lesson> findAllByOutletId(String outletId);

    /**
     * Returns lessons in the given tenant + outlet whose time window overlaps the
     * half-open interval [startInclusive, endExclusive). Excludes lessons in
     * CANCELLED state. Results are ordered by lessonStartTimestamptz ascending.
     *
     * tenantId is required explicitly because the existing JPA `@Query`
     * convention in this codebase (see `findActiveOverlappingLessonsByStudent`)
     * passes it as a parameter rather than relying on a Hibernate filter.
     */
    List<Lesson> findAllByOutletIdAndTimeRange(String tenantId,
                                               String outletId,
                                               Instant startInclusive,
                                               Instant endExclusive);

    List<Lesson> findAllByTenantId(String tenantId);

    List<Lesson> findAllByEducatorId(String educatorId);

    List<Lesson> findAllByStudentId(String studentId);

    void deleteById(String id);

    void deleteAllLessonLinksByLessonId(String lessonId);

    List<Lesson> findActiveOverlappingForStudent(
            String tenantId,
            String studentId,
            Instant start,
            Instant end,
            @Nullable String excludeLessonId);

    List<Lesson> findActiveOverlappingForEducator(
            String tenantId,
            String educatorId,
            Instant start,
            Instant end,
            @Nullable String excludeLessonId);
}
