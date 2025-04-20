package com.reportai.www.reportapi.services.outlets;

import com.reportai.www.reportapi.entities.Outlet;
import com.reportai.www.reportapi.entities.OutletRoom;
import com.reportai.www.reportapi.entities.lessons.Lesson;
import com.reportai.www.reportapi.entities.lessons.LessonOutletRoomBooking;
import com.reportai.www.reportapi.repositories.LessonOutletRoomBookingRepository;
import com.reportai.www.reportapi.repositories.OutletRoomRepository;
import com.reportai.www.reportapi.services.common.BaseServiceTemplate;
import jakarta.transaction.Transactional;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class OutletRoomService implements BaseServiceTemplate<OutletRoom, UUID> {
    private final OutletRoomRepository outletRoomRepository;
    private final LessonOutletRoomBookingRepository lessonOutletRoomBookingRepository;
    private final OutletsService outletsService;

    @Autowired
    public OutletRoomService(OutletRoomRepository outletRoomRepository, LessonOutletRoomBookingRepository lessonOutletRoomBookingRepository, OutletsService outletsService) {
        this.outletRoomRepository = outletRoomRepository;
        this.lessonOutletRoomBookingRepository = lessonOutletRoomBookingRepository;
        this.outletsService = outletsService;
    }

    @Override
    public JpaRepository<OutletRoom, UUID> getRepository() {
        return outletRoomRepository;
    }

    @Transactional
    public LessonOutletRoomBooking bookRoom(UUID outletRoomId, Lesson lesson) {
        OutletRoom outletRoom = findById(outletRoomId);

        LessonOutletRoomBooking lessonOutletRoomBooking = LessonOutletRoomBooking
                .builder()
                .outletRoom(outletRoom)
                .lesson(lesson)
                .startTimestampz(lesson.getLessonStartTimestamptz())
                .endTimestampz(lesson.getLessonEndTimestamptz())
                .build();
        lesson.setLessonOutletRoomBooking(lessonOutletRoomBooking);
        return lessonOutletRoomBookingRepository.save(lessonOutletRoomBooking);
    }

    @Transactional
    public OutletRoom create(UUID outletId, OutletRoom outletRoom) {
        Outlet outlet = outletsService.findById(outletId);
        outletRoom.addOutlet(outlet);
        return outletRoomRepository.save(outletRoom);
    }

    @Transactional
    public Set<OutletRoom> getAllInOutlet(UUID outletId) {
        return outletsService.findById(outletId).getOutletRooms();
    }

}
