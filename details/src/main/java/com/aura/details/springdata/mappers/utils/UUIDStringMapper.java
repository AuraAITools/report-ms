package com.aura.details.springdata.mappers.utils;

import java.util.UUID;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

public interface UUIDStringMapper {
    UUIDStringMapper INSTANCE = Mappers.getMapper(UUIDStringMapper.class);

    default String toUuid(UUID uuid) {
        return uuid.toString();
    }

    @Named("toUuid")
    default UUID toUuid(String uuid) {
        return UUID.fromString(uuid);
    }
}
