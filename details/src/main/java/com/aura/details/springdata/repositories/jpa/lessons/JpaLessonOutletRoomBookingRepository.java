package com.aura.details.springdata.repositories.jpa.lessons;

import com.aura.details.springdata.models.lessons.LessonOutletRoomBookingModel;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLessonOutletRoomBookingRepository extends JpaRepository<LessonOutletRoomBookingModel, UUID>, JpaSpecificationExecutor<LessonOutletRoomBookingModel> {

    List<LessonOutletRoomBookingModel> findAllByLesson_IdIn(List<UUID> lessonIds);

    Optional<LessonOutletRoomBookingModel> findByLesson_Id(UUID lessonId);

    void deleteByLesson_Id(UUID lessonId);

    @Query("""
            SELECT b FROM LessonOutletRoomBookingModel b
            WHERE b.tenantId = :tenantId
              AND b.outletRoom.id = :outletRoomId
              AND b.startTimestampz < :end
              AND b.endTimestampz > :start
              AND b.lesson.state <> com.aura.details.springdata.models.lessons.LessonModel.LESSON_STATE.CANCELLED
              AND (:excludeLessonId IS NULL OR b.lesson.id <> :excludeLessonId)
            """)
    List<LessonOutletRoomBookingModel> findOverlappingByRoomAndTenant(
            @Param("tenantId") String tenantId,
            @Param("outletRoomId") UUID outletRoomId,
            @Param("start") Instant start,
            @Param("end") Instant end,
            @Param("excludeLessonId") UUID excludeLessonId);
}
