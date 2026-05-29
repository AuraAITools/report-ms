package com.aura.details.springdata.repositories.jpa.lessons;

import com.aura.details.springdata.models.lessons.LessonOutletRoomBookingModel;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLessonOutletRoomBookingRepository extends JpaRepository<LessonOutletRoomBookingModel, UUID>, JpaSpecificationExecutor<LessonOutletRoomBookingModel> {

    List<LessonOutletRoomBookingModel> findAllByLesson_IdIn(List<UUID> lessonIds);
}

