package com.aura.core.usecases.lessons;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.aura.core.entities.lessons.Lesson;
import com.aura.core.entities.lessons.LessonOutletRoomBooking;
import com.aura.core.exceptions.LessonConflictException;
import com.aura.core.exceptions.ResourceNotFoundException;
import com.aura.core.repositories.LessonOutletRoomBookingRepository;
import com.aura.core.repositories.LessonRepository;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("ChangeLessonOutletRoomUseCaseImpl")
class ChangeLessonOutletRoomUseCaseImplTest {

    private static final String LESSON_ID = "11111111-1111-1111-1111-111111111111";

    @Mock private DetectLessonConflictsUseCase detectLessonConflictsUseCase;
    @Mock private LessonRepository lessonRepository;
    @Mock private LessonOutletRoomBookingRepository bookingRepository;

    @InjectMocks private ChangeLessonOutletRoomUseCaseImpl useCase;

    @Test
    @DisplayName("Should clear booking and return lesson without booking when outletRoomId is null")
    void clearsBookingWhenRoomNull() {
        Lesson lesson = lesson();
        lesson.setLessonOutletRoomBooking(LessonOutletRoomBooking.builder()
                .lessonId(LESSON_ID)
                .outletRoomId("room-old")
                .build());
        when(lessonRepository.findById(UUID.fromString(LESSON_ID))).thenReturn(lesson);

        Lesson result = useCase.changeLessonOutletRoom(LESSON_ID, null);

        verify(bookingRepository).deleteByLessonId(LESSON_ID);
        verify(bookingRepository, never()).save(any());
        assertThat(result).isSameAs(lesson);
        assertThat(result.getLessonOutletRoomBooking()).isNull();
    }

    @Test
    @DisplayName("Should throw when target lesson does not exist")
    void throwsWhenLessonMissing() {
        when(lessonRepository.findById(any())).thenReturn(null);

        assertThatThrownBy(() -> useCase.changeLessonOutletRoom(LESSON_ID, "room-1"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Should throw LessonConflictException(ROOM) and not save booking when conflicts exist")
    void rejectsOnConflict() {
        Lesson lesson = lesson();
        when(lessonRepository.findById(UUID.fromString(LESSON_ID))).thenReturn(lesson);
        when(detectLessonConflictsUseCase.detectRoomConflicts(
                eq("tenant-1"), eq("room-1"),
                eq(lesson.getLessonStartTimestamptz()), eq(lesson.getLessonEndTimestamptz()),
                eq(LESSON_ID)))
                .thenReturn(List.of("conflict-1"));

        assertThatThrownBy(() -> useCase.changeLessonOutletRoom(LESSON_ID, "room-1"))
                .isInstanceOf(LessonConflictException.class);

        verify(bookingRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should upsert booking when no conflicts")
    void upsertsBookingOnHappyPath() {
        Lesson lesson = lesson();
        when(lessonRepository.findById(UUID.fromString(LESSON_ID))).thenReturn(lesson);
        when(detectLessonConflictsUseCase.detectRoomConflicts(
                any(), any(), any(), any(), any())).thenReturn(List.of());

        useCase.changeLessonOutletRoom(LESSON_ID, "room-1");

        verify(bookingRepository).deleteByLessonId(LESSON_ID);
        verify(bookingRepository).save(any(LessonOutletRoomBooking.class));
    }

    @Test
    @DisplayName("Should return lesson carrying the newly saved booking when room changes")
    void returnsLessonWithSavedBookingOnChange() {
        Lesson lesson = lesson();
        when(lessonRepository.findById(UUID.fromString(LESSON_ID))).thenReturn(lesson);
        when(detectLessonConflictsUseCase.detectRoomConflicts(
                any(), any(), any(), any(), any())).thenReturn(List.of());
        LessonOutletRoomBooking savedBooking = LessonOutletRoomBooking.builder()
                .id("22222222-2222-2222-2222-222222222222")
                .lessonId(LESSON_ID)
                .outletRoomId("room-1")
                .build();
        when(bookingRepository.save(any(LessonOutletRoomBooking.class))).thenReturn(savedBooking);

        Lesson result = useCase.changeLessonOutletRoom(LESSON_ID, "room-1");

        assertThat(result).isSameAs(lesson);
        assertThat(result.getLessonOutletRoomBooking()).isSameAs(savedBooking);
    }

    private Lesson lesson() {
        return Lesson.builder()
                .id(LESSON_ID)
                .tenantId("tenant-1")
                .lessonStartTimestamptz(Instant.parse("2026-06-10T09:00:00Z"))
                .lessonEndTimestamptz(Instant.parse("2026-06-10T10:00:00Z"))
                .build();
    }
}
