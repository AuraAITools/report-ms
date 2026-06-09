package com.aura.core.usecases.lessons;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.aura.core.entities.lessons.Lesson;
import com.aura.core.entities.lessons.LessonOutletRoomBooking;
import com.aura.core.repositories.LessonOutletRoomBookingRepository;
import com.aura.core.repositories.LessonRepository;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("DetectLessonConflictsUseCaseImpl")
class DetectLessonConflictsUseCaseImplTest {

    @Mock private LessonOutletRoomBookingRepository bookingRepository;
    @Mock private LessonRepository lessonRepository;
    @InjectMocks private DetectLessonConflictsUseCaseImpl useCase;

    private final Instant start = Instant.parse("2026-06-10T09:00:00Z");
    private final Instant end = Instant.parse("2026-06-10T10:00:00Z");

    @Test
    @DisplayName("Should return conflicting lesson IDs for room conflicts")
    void detectRoomConflicts_returnsConflictingIds() {
        when(bookingRepository.findOverlappingByRoomAndTenant(
                "tenant-1", "room-1", start, end, null))
                .thenReturn(List.of(
                        LessonOutletRoomBooking.builder().lessonId("lesson-a").build(),
                        LessonOutletRoomBooking.builder().lessonId("lesson-b").build()));

        List<String> result = useCase.detectRoomConflicts(
                "tenant-1", "room-1", start, end, null);

        assertThat(result).containsExactlyInAnyOrder("lesson-a", "lesson-b");
    }

    @Test
    @DisplayName("Should return empty list when no room conflicts")
    void detectRoomConflicts_empty() {
        when(bookingRepository.findOverlappingByRoomAndTenant(
                "tenant-1", "room-1", start, end, "exclude-1"))
                .thenReturn(List.of());

        List<String> result = useCase.detectRoomConflicts(
                "tenant-1", "room-1", start, end, "exclude-1");

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should return conflicting lesson IDs for student conflicts")
    void detectStudentConflicts_returnsConflictingIds() {
        when(lessonRepository.findActiveOverlappingForStudent(
                "tenant-1", "student-1", start, end, null))
                .thenReturn(List.of(lesson("lesson-x"), lesson("lesson-y")));

        List<String> result = useCase.detectStudentConflicts(
                "tenant-1", "student-1", start, end, null);

        assertThat(result).containsExactlyInAnyOrder("lesson-x", "lesson-y");
    }

    @Test
    @DisplayName("Should return conflicting lesson IDs for educator conflicts")
    void detectEducatorConflicts_returnsConflictingIds() {
        when(lessonRepository.findActiveOverlappingForEducator(
                "tenant-1", "educator-1", start, end, "exclude-1"))
                .thenReturn(List.of(lesson("lesson-z")));

        List<String> result = useCase.detectEducatorConflicts(
                "tenant-1", "educator-1", start, end, "exclude-1");

        assertThat(result).containsExactly("lesson-z");
    }

    private static Lesson lesson(String id) {
        return Lesson.builder().id(id).build();
    }
}
