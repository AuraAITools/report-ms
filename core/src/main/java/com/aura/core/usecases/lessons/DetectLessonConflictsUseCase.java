package com.aura.core.usecases.lessons;

import java.time.Instant;
import java.util.List;
import org.jspecify.annotations.Nullable;

public interface DetectLessonConflictsUseCase {

    List<String> detectRoomConflicts(
            String tenantId,
            String outletRoomId,
            Instant start,
            Instant end,
            @Nullable String excludeLessonId);

    List<String> detectStudentConflicts(
            String tenantId,
            String studentId,
            Instant start,
            Instant end,
            @Nullable String excludeLessonId);

    List<String> detectEducatorConflicts(
            String tenantId,
            String educatorId,
            Instant start,
            Instant end,
            @Nullable String excludeLessonId);
}
