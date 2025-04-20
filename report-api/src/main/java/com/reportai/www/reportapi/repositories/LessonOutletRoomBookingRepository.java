package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.lessons.LessonOutletRoomBooking;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LessonOutletRoomBookingRepository extends JpaRepository<LessonOutletRoomBooking, UUID>, JpaSpecificationExecutor<LessonOutletRoomBooking> {
    @Query("SELECT lorb FROM LessonOutletRoomBooking lorb WHERE lorb.lesson.id = :lessonId AND lorb.tenantId = :tenantId")
    Optional<LessonOutletRoomBooking> findByLessonIdAndTenantId(@Param("lessonId") UUID lessonId, @Param("tenantId") String tenantId);

}