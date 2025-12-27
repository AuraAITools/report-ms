package com.aura.details.mappers.lessons;

import com.aura.core.entities.lessons.LessonOutletRoomBooking;
import com.aura.details.mappers.lessons.LessonMapper;
import com.aura.details.mappers.outlets.OutletRoomMapper;
import com.aura.details.tables.lessons.LessonOutletRoomBookingTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {LessonMapper.class, OutletRoomMapper.class})
public interface LessonOutletRoomBookingMapper {
    LessonOutletRoomBookingMapper INSTANCE = Mappers.getMapper(LessonOutletRoomBookingMapper.class);

    LessonOutletRoomBooking toDomainEntity(LessonOutletRoomBookingTable lessonOutletRoomBookingTable);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "Id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    LessonOutletRoomBookingTable toTableEntity(LessonOutletRoomBooking lessonOutletRoomBooking);
}

