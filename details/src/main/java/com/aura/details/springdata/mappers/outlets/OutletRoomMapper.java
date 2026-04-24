package com.aura.details.springdata.mappers.outlets;

import com.aura.core.entities.outlets.OutletRoom;
import com.aura.details.springdata.models.outlets.OutletRoomModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {OutletMapper.class})
public interface OutletRoomMapper {
    OutletRoomMapper INSTANCE = Mappers.getMapper(OutletRoomMapper.class);

    @Mapping(target = "lessonOutletRoomBookings", ignore = true)
    OutletRoom toDomainEntity(OutletRoomModel outletRoomModel);

    @Mapping(target = "lessonOutletRoomBookings", ignore = true)
    OutletRoomModel toModelEntity(OutletRoom outletRoom);
}

