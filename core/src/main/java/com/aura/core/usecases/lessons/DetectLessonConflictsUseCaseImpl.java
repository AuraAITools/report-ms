package com.aura.core.usecases.lessons;

import com.aura.core.entities.lessons.Lesson;
import com.aura.core.entities.lessons.LessonOutletRoomBooking;
import com.aura.core.repositories.LessonOutletRoomBookingRepository;
import com.aura.core.repositories.LessonRepository;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class DetectLessonConflictsUseCaseImpl implements DetectLessonConflictsUseCase {

    private final LessonOutletRoomBookingRepository lessonOutletRoomBookingRepository;
    private final LessonRepository lessonRepository;

    @Override
    public List<String> detectRoomConflicts(String tenantId, String outletRoomId,
                                            Instant start, Instant end,
                                            @Nullable String excludeLessonId) {
        return lessonOutletRoomBookingRepository
                .findOverlappingByRoomAndTenant(tenantId, outletRoomId, start, end, excludeLessonId)
                .stream()
                .map(LessonOutletRoomBooking::getLessonId)
                .toList();
    }

    @Override
    public List<String> detectStudentConflicts(String tenantId, String studentId,
                                               Instant start, Instant end,
                                               @Nullable String excludeLessonId) {
        return lessonRepository
                .findActiveOverlappingForStudent(tenantId, studentId, start, end, excludeLessonId)
                .stream()
                .map(Lesson::getId)
                .toList();
    }

    @Override
    public List<String> detectEducatorConflicts(String tenantId, String educatorId,
                                                Instant start, Instant end,
                                                @Nullable String excludeLessonId) {
        return lessonRepository
                .findActiveOverlappingForEducator(tenantId, educatorId, start, end, excludeLessonId)
                .stream()
                .map(Lesson::getId)
                .toList();
    }
}
