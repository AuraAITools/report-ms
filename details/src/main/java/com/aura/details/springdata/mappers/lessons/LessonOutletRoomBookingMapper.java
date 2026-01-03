package com.aura.details.springdata.mappers.lessons;

import com.aura.core.entities.lessons.LessonOutletRoomBooking;
import com.aura.details.springdata.mappers.outlets.OutletRoomMapper;
import com.aura.details.springdata.tables.lessons.LessonOutletRoomBookingTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {LessonMapper.class, OutletRoomMapper.class})
public interface LessonOutletRoomBookingMapper {
    LessonOutletRoomBookingMapper INSTANCE = Mappers.getMapper(LessonOutletRoomBookingMapper.class);

    LessonOutletRoomBooking toDomainEntity(LessonOutletRoomBookingTable lessonOutletRoomBookingTable);

    @Mapping(target = "Id", source = "id")
    LessonOutletRoomBookingTable toTableEntity(LessonOutletRoomBooking lessonOutletRoomBooking);
}

