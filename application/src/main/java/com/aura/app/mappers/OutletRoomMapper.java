package com.aura.app.mappers;

import com.aura.app.codegen.types.CreateOutletRoomInput;
import com.aura.app.codegen.types.OutletRoomResponse;
import com.aura.app.codegen.types.UpdateOutletRoomInput;
import com.aura.core.entities.outlets.OutletRoom;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {DateToEpochMapper.class, AccountMinimalDetailMapper.class})
public interface OutletRoomMapper {
    OutletRoomMapper INSTANCE = Mappers.getMapper(OutletRoomMapper.class);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "institutionId", target = "tenantId")
    OutletRoom toDomainEntity(CreateOutletRoomInput createOutletRoomInput);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "outletId", ignore = true)
    @Mapping(source = "institutionId", target = "tenantId")
    OutletRoom toDomainEntity(UpdateOutletRoomInput updateOutletRoomInput);

    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "toMinimal")
    @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "toMinimal")
    OutletRoomResponse toDto(OutletRoom outletRoom);
}
