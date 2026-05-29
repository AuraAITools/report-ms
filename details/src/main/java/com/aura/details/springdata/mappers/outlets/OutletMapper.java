package com.aura.details.springdata.mappers.outlets;

import com.aura.core.entities.outlets.Outlet;
import com.aura.details.springdata.models.outlets.OutletModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {OutletRoomMapper.class})
public interface OutletMapper {
    OutletMapper INSTANCE = Mappers.getMapper(OutletMapper.class);

    Outlet toDomainEntity(OutletModel outletModel);

    OutletModel toModelEntity(Outlet outlet);
}
