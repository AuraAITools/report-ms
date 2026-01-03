package com.aura.details.springdata.mappers.outlets;

import com.aura.core.entities.outlets.Outlet;
import com.aura.details.springdata.tables.outlets.OutletTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OutletMapper {
    OutletMapper INSTANCE = Mappers.getMapper(OutletMapper.class);

    Outlet toDomainEntity(OutletTable outletTable);

    @Mapping(target = "Id", source = "id")
    @Mapping(target = "studentOutletLinks", ignore = true)
    @Mapping(target = "outletEducatorLinks", ignore = true)
    @Mapping(target = "courses", ignore = true)
    @Mapping(target = "outletRoomTables", ignore = true)
    @Mapping(target = "lessons", ignore = true)
    OutletTable toTableEntity(Outlet outlet);
}

