package com.aura.core.repositories;

import com.aura.core.entities.lessons.LessonOutletRoomBooking;
import java.util.List;
import java.util.Map;

public interface LessonOutletRoomBookingRepository {
    LessonOutletRoomBooking save(LessonOutletRoomBooking booking);

    Map<String, LessonOutletRoomBooking> findAllByLessonIds(List<String> lessonIds);
}
