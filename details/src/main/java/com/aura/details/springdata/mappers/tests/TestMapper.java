package com.aura.details.springdata.mappers.tests;

import com.aura.core.entities.tests.Test;
import com.aura.details.springdata.tables.tests.TestTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {TestGroupMapper.class})
public interface TestMapper {
    TestMapper INSTANCE = Mappers.getMapper(TestMapper.class);

    @Mapping(target = "testResults", ignore = true)
    Test toDomainEntity(TestTable testTable);

    @Mapping(target = "testResults", ignore = true)
    @Mapping(target = "Id", source = "id")
    TestTable toTableEntity(Test test);
}

