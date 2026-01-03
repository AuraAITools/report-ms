package com.aura.app.mappers;

import com.aura.app.codegen.types.CreateOutletInput;
import com.aura.app.codegen.types.OutletResponse;
import com.aura.core.entities.outlets.Outlet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {DateToEpochMapper.class})
public interface OutletMapper {
    public static OutletMapper INSTANCE = Mappers.getMapper(OutletMapper.class);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    Outlet toDomainEntity(CreateOutletInput createOutletInput, String tenantId);

    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    OutletResponse toDto(Outlet outlet);
}
