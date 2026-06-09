package com.aura.details.springdata.repositories.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.aura.core.entities.lessons.LessonOutletRoomBooking;
import com.aura.details.springdata.models.lessons.LessonModel;
import com.aura.details.springdata.models.lessons.LessonOutletRoomBookingModel;
import com.aura.details.springdata.repositories.jpa.lessons.JpaLessonOutletRoomBookingRepository;
import jakarta.persistence.EntityManager;
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
@DisplayName("LessonOutletRoomBookingRepositoryImpl")
class LessonOutletRoomBookingRepositoryImplTest {

    @Mock private JpaLessonOutletRoomBookingRepository jpaRepository;
    @Mock private EntityManager entityManager;

    @InjectMocks private LessonOutletRoomBookingRepositoryImpl repository;

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
    @DisplayName("Should pass null excludeLessonId when none provided")
    void passesNullExclude() {
        UUID outletRoomId = UUID.randomUUID();
        Instant start = Instant.parse("2026-06-10T09:00:00Z");
        Instant end = Instant.parse("2026-06-10T10:00:00Z");

        when(jpaRepository.findOverlappingByRoomAndTenant(
                "tenant-1", outletRoomId, start, end, null))
                .thenReturn(List.of());

        List<LessonOutletRoomBooking> result = repository.findOverlappingByRoomAndTenant(
                "tenant-1", outletRoomId.toString(), start, end, null);

        assertThat(result).isEmpty();
    }
}
