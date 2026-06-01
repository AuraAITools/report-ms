package com.aura.app.mappers;

import com.aura.app.codegen.types.CreatePriceRecordInput;
import com.aura.app.codegen.types.PriceRecordResponse;
import com.aura.core.entities.lessons.PriceRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {DateToEpochMapper.class, AccountMinimalDetailMapper.class})
public interface PriceRecordMapper {
    PriceRecordMapper INSTANCE = Mappers.getMapper(PriceRecordMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "courseId", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    PriceRecord toDomainEntity(CreatePriceRecordInput createPriceRecordInput, String tenantId);

    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "toMinimal")
    @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "toMinimal")
    PriceRecordResponse toDto(PriceRecord createPriceRecord);
}
