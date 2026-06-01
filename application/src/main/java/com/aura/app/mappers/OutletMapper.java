package com.aura.app.mappers;

import com.aura.app.codegen.types.CreateOutletInput;
import com.aura.app.codegen.types.OutletResponse;
import com.aura.app.codegen.types.UpdateOutletInput;
import com.aura.core.entities.outlets.Outlet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {DateToEpochMapper.class, AccountMinimalDetailMapper.class})
public interface OutletMapper {
    OutletMapper INSTANCE = Mappers.getMapper(OutletMapper.class);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "institutionId", target = "tenantId")
    Outlet toDomainEntity(CreateOutletInput createOutletInput);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "outletRoom", ignore = true)
    @Mapping(source = "institutionId", target = "tenantId")
    Outlet toDomainEntity(UpdateOutletInput updateOutletInput);

    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "toMinimal")
    @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "toMinimal")
    OutletResponse toDto(Outlet outlet);
}
