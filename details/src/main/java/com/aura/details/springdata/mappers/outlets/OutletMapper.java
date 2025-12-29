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

    @Mapping(target = "studentOutletLinks", ignore = true)
    @Mapping(target = "outletEducatorLinks", ignore = true)
    @Mapping(target = "courses", ignore = true)
    @Mapping(target = "outletRoomTables", ignore = true)
    @Mapping(target = "lessons", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "Id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    OutletTable toTableEntity(Outlet outlet);
}

