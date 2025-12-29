package com.aura.details.mappers.tests;

import com.aura.core.entities.tests.Test;
import com.aura.details.tables.tests.TestTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {TestGroupMapper.class})
public interface TestMapper {
    TestMapper INSTANCE = Mappers.getMapper(TestMapper.class);

    @Mapping(target = "testResults", ignore = true)
    Test toDomainEntity(TestTable testTable);

    @Mapping(target = "testResults", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "Id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    TestTable toTableEntity(Test test);
}

