package com.reportai.www.reportapi.services.outlets;

import com.reportai.www.reportapi.entities.Outlet;
import com.reportai.www.reportapi.entities.OutletRoom;
import com.reportai.www.reportapi.entities.lessons.Lesson;
import com.reportai.www.reportapi.entities.lessons.LessonOutletRoomBooking;
import com.reportai.www.reportapi.exceptions.lib.ResourceAlreadyExistsException;
import com.reportai.www.reportapi.repositories.LessonOutletRoomBookingRepository;
import com.reportai.www.reportapi.repositories.OutletRoomRepository;
import com.reportai.www.reportapi.services.common.ISimpleRead;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class OutletRoomService implements ISimpleRead<OutletRoom> {
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

        // if outlet room is already booked at this timing, fail booking
        List<LessonOutletRoomBooking> overlappingOutletRoomBookings = lessonOutletRoomBookingRepository.findOverlappingOutletRoomBookingsByTime(outletRoom.getId(), lesson.getLessonStartTimestamptz(), lesson.getLessonEndTimestamptz());
        if (!overlappingOutletRoomBookings.isEmpty()) {
            throw new ResourceAlreadyExistsException("outlet room is already booked at this timing");
        }
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
