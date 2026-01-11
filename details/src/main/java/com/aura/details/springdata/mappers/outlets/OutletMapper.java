package com.aura.details.springdata.mappers.outlets;

import com.aura.core.entities.outlets.Outlet;
import com.aura.details.springdata.tables.outlets.OutletTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OutletMapper {
    OutletMapper INSTANCE = Mappers.getMapper(OutletMapper.class);

    @Mapping(target = "studentOutletLinks", ignore = true)
    @Mapping(target = "outletEducatorLinks", ignore = true)
    @Mapping(target = "courses", ignore = true)
    @Mapping(target = "outletRoom", ignore = true)
    @Mapping(target = "lessons", ignore = true)
    Outlet toDomainEntity(OutletTable outletTable);

    @Mapping(target = "studentOutletLinks", ignore = true)
    @Mapping(target = "outletEducatorLinks", ignore = true)
    @Mapping(target = "courses", ignore = true)
    @Mapping(target = "outletRoom", ignore = true)
    @Mapping(target = "lessons", ignore = true)
    @Mapping(target = "Id", source = "id")
    OutletTable toTableEntity(Outlet outlet);
}

