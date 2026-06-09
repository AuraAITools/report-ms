package com.aura.core.usecases.lessons;

import com.aura.core.entities.lessons.Lesson;
import com.aura.core.entities.lessons.LessonOutletRoomBooking;
import com.aura.core.exceptions.LessonConflictException;
import com.aura.core.exceptions.ResourceNotFoundException;
import com.aura.core.repositories.LessonOutletRoomBookingRepository;
import com.aura.core.repositories.LessonRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class ChangeLessonOutletRoomUseCaseImpl implements ChangeLessonOutletRoomUseCase {

    private final DetectLessonConflictsUseCase detectLessonConflictsUseCase;
    private final LessonRepository lessonRepository;
    private final LessonOutletRoomBookingRepository lessonOutletRoomBookingRepository;

    @Override
    public Lesson changeLessonOutletRoom(String lessonId, @Nullable String outletRoomId) {
        Lesson lesson = lessonRepository.findById(UUID.fromString(lessonId));
        if (lesson == null) {
            throw new ResourceNotFoundException("Lesson", lessonId);
        }

        if (outletRoomId == null) {
            lessonOutletRoomBookingRepository.deleteByLessonId(lessonId);
            return lesson;
        }

        List<String> conflicts = detectLessonConflictsUseCase.detectRoomConflicts(
                lesson.getTenantId(),
                outletRoomId,
                lesson.getLessonStartTimestamptz(),
                lesson.getLessonEndTimestamptz(),
                lessonId);

        if (!conflicts.isEmpty()) {
            throw new LessonConflictException(
                    LessonConflictException.ConflictType.ROOM, conflicts, null);
        }

        lessonOutletRoomBookingRepository.deleteByLessonId(lessonId);
        LessonOutletRoomBooking newBooking = LessonOutletRoomBooking.builder()
                .tenantId(lesson.getTenantId())
                .lessonId(lessonId)
                .outletRoomId(outletRoomId)
                .startTimestampz(lesson.getLessonStartTimestamptz())
                .endTimestampz(lesson.getLessonEndTimestamptz())
                .build();
        lessonOutletRoomBookingRepository.save(newBooking);
        return lesson;
    }
}
