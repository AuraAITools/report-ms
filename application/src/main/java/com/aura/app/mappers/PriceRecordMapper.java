package com.aura.app.mappers;

import com.aura.app.codegen.types.CreatePriceRecordInput;
import com.aura.app.codegen.types.PriceRecordResponse;
import com.aura.core.entities.lessons.PriceRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {DateToEpochMapper.class})
public interface PriceRecordMapper {
    PriceRecordMapper INSTANCE = Mappers.getMapper(PriceRecordMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    PriceRecord toDomainEntity(CreatePriceRecordInput createPriceRecordInput, String tenantId);

    PriceRecordResponse toDto(PriceRecord createPriceRecord);
}
