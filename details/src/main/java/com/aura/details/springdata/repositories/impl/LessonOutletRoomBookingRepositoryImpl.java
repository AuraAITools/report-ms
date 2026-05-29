package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.lessons.LessonOutletRoomBooking;
import com.aura.core.repositories.LessonOutletRoomBookingRepository;
import com.aura.details.springdata.mappers.lessons.LessonOutletRoomBookingMapper;
import com.aura.details.springdata.models.lessons.LessonModel;
import com.aura.details.springdata.models.lessons.LessonOutletRoomBookingModel;
import com.aura.details.springdata.models.outlets.OutletRoomModel;
import com.aura.details.springdata.repositories.jpa.lessons.JpaLessonOutletRoomBookingRepository;
import jakarta.persistence.EntityManager;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LessonOutletRoomBookingRepositoryImpl implements LessonOutletRoomBookingRepository {
    private final JpaLessonOutletRoomBookingRepository jpaRepository;
    private final EntityManager entityManager;

    @Override
    public LessonOutletRoomBooking save(LessonOutletRoomBooking booking) {
        LessonOutletRoomBookingModel model = LessonOutletRoomBookingMapper.INSTANCE.toModelEntity(booking);
        model.setLesson(Refs.ref(entityManager, LessonModel.class, booking.getLessonId()));
        model.setOutletRoom(Refs.ref(entityManager, OutletRoomModel.class, booking.getOutletRoomId()));
        return LessonOutletRoomBookingMapper.INSTANCE.toDomainEntity(jpaRepository.save(model));
    }

    @Override
    public Map<String, LessonOutletRoomBooking> findAllByLessonIds(List<String> lessonIds) {
        if (lessonIds.isEmpty()) return Collections.emptyMap();
        List<UUID> uuids = lessonIds.stream().map(UUID::fromString).toList();
        return jpaRepository.findAllByLesson_IdIn(uuids).stream()
                .map(LessonOutletRoomBookingMapper.INSTANCE::toDomainEntity)
                .collect(Collectors.toMap(LessonOutletRoomBooking::getLessonId, b -> b));
    }
}
