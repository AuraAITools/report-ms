package com.aura.core.repositories;

import com.aura.core.entities.lessons.LessonOutletRoomBooking;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.jspecify.annotations.Nullable;

public interface LessonOutletRoomBookingRepository {
    LessonOutletRoomBooking save(LessonOutletRoomBooking booking);

    Map<String, LessonOutletRoomBooking> findAllByLessonIds(List<String> lessonIds);

    @Nullable LessonOutletRoomBooking findByLessonId(String lessonId);

    void deleteByLessonId(String lessonId);

    List<LessonOutletRoomBooking> findOverlappingByRoomAndTenant(
            String tenantId,
            String outletRoomId,
            Instant start,
            Instant end,
            @Nullable String excludeLessonId);
}
