package com.aura.details.springdata.mappers.tests;

import com.aura.core.entities.tests.TestGroup;
import com.aura.details.springdata.tables.tests.TestGroupTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TestGroupMapper {
    TestGroupMapper INSTANCE = Mappers.getMapper(TestGroupMapper.class);

    @Mapping(target = "tests", ignore = true)
    @Mapping(target = "subjectTestGroupLinks", ignore = true)
    TestGroup toDomainEntity(TestGroupTable testGroupTable);

    @Mapping(target = "tests", ignore = true)
    @Mapping(target = "subjectTestGroupLinks", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "Id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    TestGroupTable toTableEntity(TestGroup testGroup);
}

