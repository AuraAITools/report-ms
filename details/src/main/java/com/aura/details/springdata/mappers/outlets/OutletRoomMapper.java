package com.aura.details.springdata.mappers.outlets;

import com.aura.core.entities.outlets.OutletRoom;
import com.aura.details.springdata.models.outlets.OutletModel;
import com.aura.details.springdata.models.outlets.OutletRoomModel;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OutletRoomMapper {
    OutletRoomMapper INSTANCE = Mappers.getMapper(OutletRoomMapper.class);

    @Mapping(source = "outlet.id", target = "outletId")
    OutletRoom toDomainEntity(OutletRoomModel outletRoomModel);

    @Mapping(source = "outletId", target = "outlet", qualifiedByName = "outletRef")
    OutletRoomModel toModelEntity(OutletRoom outletRoom);

    @Named("outletRef")
    default OutletModel outletRef(String outletId) {
        if (outletId == null) {
            return null;
        }
        return OutletModel.builder().id(UUID.fromString(outletId)).build();
    }
}
