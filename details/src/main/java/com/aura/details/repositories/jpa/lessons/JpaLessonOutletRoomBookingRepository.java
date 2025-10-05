package com.aura.details.repositories.jpa.lessons;

import com.aura.details.tables.lessons.LessonOutletRoomBookingTable;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLessonOutletRoomBookingRepository extends JpaRepository<LessonOutletRoomBookingTable, UUID>, JpaSpecificationExecutor<LessonOutletRoomBookingTable> {
}

