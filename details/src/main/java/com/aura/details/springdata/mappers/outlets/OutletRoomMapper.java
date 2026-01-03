package com.aura.details.springdata.mappers.outlets;

import com.aura.core.entities.outlets.OutletRoom;
import com.aura.details.springdata.tables.outlets.OutletRoomTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {OutletMapper.class})
public interface OutletRoomMapper {
    OutletRoomMapper INSTANCE = Mappers.getMapper(OutletRoomMapper.class);

    @Mapping(target = "lessonOutletRoomBookings", ignore = true)
    OutletRoom toDomainEntity(OutletRoomTable outletRoomTable);

    @Mapping(target = "lessonOutletRoomBookings", ignore = true)
    @Mapping(target = "Id", source = "id")
    OutletRoomTable toTableEntity(OutletRoom outletRoom);
}

