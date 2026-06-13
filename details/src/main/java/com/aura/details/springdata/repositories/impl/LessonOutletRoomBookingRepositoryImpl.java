package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.lessons.LessonOutletRoomBooking;
import com.aura.core.repositories.LessonOutletRoomBookingRepository;
import com.aura.details.springdata.mappers.lessons.LessonOutletRoomBookingMapper;
import com.aura.details.springdata.models.lessons.LessonModel;
import com.aura.details.springdata.models.lessons.LessonOutletRoomBookingModel;
import com.aura.details.springdata.models.outlets.OutletRoomModel;
import com.aura.details.springdata.repositories.jpa.lessons.JpaLessonOutletRoomBookingRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class LessonOutletRoomBookingRepositoryImpl implements LessonOutletRoomBookingRepository {
    private final JpaLessonOutletRoomBookingRepository jpaRepository;
    private final EntityManager entityManager;

    @Override
    public LessonOutletRoomBooking save(LessonOutletRoomBooking booking) {
        LessonOutletRoomBookingModel model = LessonOutletRoomBookingMapper.INSTANCE.toModelEntity(booking);
        model.setLesson(Refs.ref(entityManager, LessonModel.class, booking.getLessonId()));
        model.setOutletRoom(Refs.ref(entityManager, OutletRoomModel.class, booking.getOutletRoomId()));
        LessonOutletRoomBookingModel saved = jpaRepository.save(model);
        // Keep the inverse side of the bidirectional one-to-one in sync. If the lesson is already
        // managed in this persistence context (e.g. loaded earlier in the transaction), leaving it
        // pointing at a stale/removed booking trips Hibernate's transient-reference check on flush.
        LessonModel lessonModel = saved.getLesson();
        if (lessonModel != null) {
            lessonModel.setLessonOutletRoomBooking(saved);
        }
        return LessonOutletRoomBookingMapper.INSTANCE.toDomainEntity(saved);
    }

    @Override
    public Map<String, LessonOutletRoomBooking> findAllByLessonIds(List<String> lessonIds) {
        if (lessonIds.isEmpty()) return Collections.emptyMap();
        List<UUID> uuids = lessonIds.stream().map(UUID::fromString).toList();
        return jpaRepository.findAllByLesson_IdIn(uuids).stream()
                .map(LessonOutletRoomBookingMapper.INSTANCE::toDomainEntity)
                .collect(Collectors.toMap(LessonOutletRoomBooking::getLessonId, b -> b));
    }

    @Override
    public @Nullable LessonOutletRoomBooking findByLessonId(String lessonId) {
        return jpaRepository.findByLesson_Id(UUID.fromString(lessonId))
                .map(LessonOutletRoomBookingMapper.INSTANCE::toDomainEntity)
                .orElse(null);
    }

    @Override
    public void deleteByLessonId(String lessonId) {
        jpaRepository.findByLesson_Id(UUID.fromString(lessonId)).ifPresent(booking -> {
            // Detach the inverse side first: the managed LessonModel may still reference this
            // booking (eager one-to-one), and a managed entity referencing a removed instance
            // fails Hibernate's transient-reference check on flush.
            LessonModel lessonModel = booking.getLesson();
            if (lessonModel != null) {
                lessonModel.setLessonOutletRoomBooking(null);
            }
            jpaRepository.delete(booking);
            // Flush so the DELETE hits the database before any subsequent booking INSERT in the
            // same transaction — Hibernate orders inserts before deletes, which would otherwise
            // violate the (lesson_id, outlet_room_id) unique constraint on re-booking.
            jpaRepository.flush();
        });
    }

    @Override
    public List<LessonOutletRoomBooking> findOverlappingByRoomAndTenant(
            String tenantId,
            String outletRoomId,
            Instant start,
            Instant end,
            @Nullable String excludeLessonId) {
        UUID roomUuid = UUID.fromString(outletRoomId);
        List<LessonOutletRoomBookingModel> overlapping = excludeLessonId == null
                ? jpaRepository.findOverlappingByRoomAndTenant(tenantId, roomUuid, start, end)
                : jpaRepository.findOverlappingByRoomAndTenant(
                        tenantId, roomUuid, start, end, UUID.fromString(excludeLessonId));
        return overlapping.stream()
                .map(LessonOutletRoomBookingMapper.INSTANCE::toDomainEntity)
                .toList();
    }
}
