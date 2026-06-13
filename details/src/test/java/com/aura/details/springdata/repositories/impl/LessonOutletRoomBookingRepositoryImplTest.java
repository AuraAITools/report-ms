package com.aura.details.springdata.repositories.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.aura.core.entities.lessons.LessonOutletRoomBooking;
import com.aura.details.springdata.models.lessons.LessonModel;
import com.aura.details.springdata.models.lessons.LessonOutletRoomBookingModel;
import com.aura.details.springdata.models.outlets.OutletRoomModel;
import com.aura.details.springdata.repositories.jpa.lessons.JpaLessonOutletRoomBookingRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("LessonOutletRoomBookingRepositoryImpl")
class LessonOutletRoomBookingRepositoryImplTest {

    @Mock private JpaLessonOutletRoomBookingRepository jpaRepository;
    @Mock private EntityManager entityManager;

    @InjectMocks private LessonOutletRoomBookingRepositoryImpl repository;

    @Test
    @DisplayName("Should sync the saved booking onto the managed lesson's inverse one-to-one side on save")
    void saveSyncsInverseSideOnLesson() {
        UUID lessonId = UUID.randomUUID();
        UUID outletRoomId = UUID.randomUUID();
        LessonModel managedLesson = LessonModel.builder().id(lessonId).build();
        OutletRoomModel managedRoom = OutletRoomModel.builder().id(outletRoomId).build();
        when(entityManager.getReference(LessonModel.class, lessonId)).thenReturn(managedLesson);
        when(entityManager.getReference(OutletRoomModel.class, outletRoomId)).thenReturn(managedRoom);
        when(jpaRepository.save(any(LessonOutletRoomBookingModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        LessonOutletRoomBooking booking = LessonOutletRoomBooking.builder()
                .tenantId("tenant-1")
                .lessonId(lessonId.toString())
                .outletRoomId(outletRoomId.toString())
                .startTimestampz(Instant.parse("2026-06-10T09:00:00Z"))
                .endTimestampz(Instant.parse("2026-06-10T10:00:00Z"))
                .build();

        LessonOutletRoomBooking result = repository.save(booking);

        assertThat(managedLesson.getLessonOutletRoomBooking()).isNotNull();
        assertThat(managedLesson.getLessonOutletRoomBooking().getLesson()).isSameAs(managedLesson);
        assertThat(result.getLessonId()).isEqualTo(lessonId.toString());
        assertThat(result.getOutletRoomId()).isEqualTo(outletRoomId.toString());
    }

    @Test
    @DisplayName("Should detach the inverse side on the lesson, delete the booking, and flush on deleteByLessonId")
    void deleteByLessonIdDetachesInverseSideAndFlushes() {
        UUID lessonId = UUID.randomUUID();
        LessonModel managedLesson = LessonModel.builder().id(lessonId).build();
        LessonOutletRoomBookingModel existing = LessonOutletRoomBookingModel.builder()
                .id(UUID.randomUUID())
                .lesson(managedLesson)
                .build();
        managedLesson.setLessonOutletRoomBooking(existing);
        when(jpaRepository.findByLesson_Id(lessonId)).thenReturn(Optional.of(existing));

        repository.deleteByLessonId(lessonId.toString());

        assertThat(managedLesson.getLessonOutletRoomBooking()).isNull();
        verify(jpaRepository).delete(existing);
        verify(jpaRepository).flush();
    }

    @Test
    @DisplayName("Should do nothing on deleteByLessonId when no booking exists for the lesson")
    void deleteByLessonIdNoOpWhenNoBooking() {
        UUID lessonId = UUID.randomUUID();
        when(jpaRepository.findByLesson_Id(lessonId)).thenReturn(Optional.empty());

        repository.deleteByLessonId(lessonId.toString());

        verify(jpaRepository, never()).delete(any(LessonOutletRoomBookingModel.class));
        verify(jpaRepository, never()).flush();
    }

    @Test
    @DisplayName("Should return overlapping bookings via JPA query (mapper applied via MapStruct INSTANCE)")
    void findsOverlapping() {
        UUID outletRoomId = UUID.randomUUID();
        UUID excludeLessonId = UUID.randomUUID();
        UUID conflictingLessonId = UUID.randomUUID();
        Instant start = Instant.parse("2026-06-10T09:00:00Z");
        Instant end = Instant.parse("2026-06-10T10:00:00Z");

        LessonModel lessonRef = LessonModel.builder().id(conflictingLessonId).build();
        LessonOutletRoomBookingModel match = LessonOutletRoomBookingModel.builder()
                .id(UUID.randomUUID())
                .tenantId("tenant-1")
                .lesson(lessonRef)
                .startTimestampz(Instant.parse("2026-06-10T09:30:00Z"))
                .endTimestampz(Instant.parse("2026-06-10T10:30:00Z"))
                .build();

        when(jpaRepository.findOverlappingByRoomAndTenant(
                "tenant-1", outletRoomId, start, end, excludeLessonId))
                .thenReturn(List.of(match));

        List<LessonOutletRoomBooking> result = repository.findOverlappingByRoomAndTenant(
                "tenant-1", outletRoomId.toString(), start, end, excludeLessonId.toString());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getLessonId()).isEqualTo(conflictingLessonId.toString());
    }

    @Test
    @DisplayName("Should dispatch to the no-exclusion query when excludeLessonId is null")
    void dispatchesToNoExcludeQueryWhenExcludeIsNull() {
        UUID outletRoomId = UUID.randomUUID();
        UUID conflictingLessonId = UUID.randomUUID();
        Instant start = Instant.parse("2026-06-10T09:00:00Z");
        Instant end = Instant.parse("2026-06-10T10:00:00Z");

        LessonModel lessonRef = LessonModel.builder().id(conflictingLessonId).build();
        LessonOutletRoomBookingModel match = LessonOutletRoomBookingModel.builder()
                .id(UUID.randomUUID())
                .tenantId("tenant-1")
                .lesson(lessonRef)
                .startTimestampz(Instant.parse("2026-06-10T09:30:00Z"))
                .endTimestampz(Instant.parse("2026-06-10T10:30:00Z"))
                .build();

        when(jpaRepository.findOverlappingByRoomAndTenant("tenant-1", outletRoomId, start, end))
                .thenReturn(List.of(match));

        List<LessonOutletRoomBooking> result = repository.findOverlappingByRoomAndTenant(
                "tenant-1", outletRoomId.toString(), start, end, null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getLessonId()).isEqualTo(conflictingLessonId.toString());
        verify(jpaRepository, never()).findOverlappingByRoomAndTenant(
                anyString(), any(UUID.class), any(Instant.class), any(Instant.class), any(UUID.class));
    }
}
