package com.aura.details.springdata.mappers.lessons;

import com.aura.core.entities.lessons.LessonOutletRoomBooking;
import com.aura.details.springdata.models.lessons.LessonModel;
import com.aura.details.springdata.models.lessons.LessonOutletRoomBookingModel;
import com.aura.details.springdata.models.outlets.OutletRoomModel;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LessonOutletRoomBookingMapper {
    LessonOutletRoomBookingMapper INSTANCE = Mappers.getMapper(LessonOutletRoomBookingMapper.class);

    @Mapping(source = "outletRoom.id", target = "outletRoomId")
    @Mapping(source = "lesson.id", target = "lessonId")
    LessonOutletRoomBooking toDomainEntity(LessonOutletRoomBookingModel model);

    @Mapping(source = "outletRoomId", target = "outletRoom", qualifiedByName = "outletRoomRef")
    @Mapping(source = "lessonId", target = "lesson", qualifiedByName = "lessonRef")
    LessonOutletRoomBookingModel toModelEntity(LessonOutletRoomBooking booking);

    @Named("outletRoomRef")
    default OutletRoomModel outletRoomRef(String outletRoomId) {
        if (outletRoomId == null) {
            return null;
        }
        return OutletRoomModel.builder().id(UUID.fromString(outletRoomId)).build();
    }

    @Named("lessonRef")
    default LessonModel lessonRef(String lessonId) {
        if (lessonId == null) {
            return null;
        }
        return LessonModel.builder().id(UUID.fromString(lessonId)).build();
    }
}
