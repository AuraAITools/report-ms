package com.aura.details.springdata.mappers.tests;

import com.aura.core.entities.tests.TestGroup;
import com.aura.details.springdata.mappers.utils.UUIDStringMapper;
import com.aura.details.springdata.tables.tests.TestGroupTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UUIDStringMapper.class})
public interface TestGroupMapper {
    TestGroupMapper INSTANCE = Mappers.getMapper(TestGroupMapper.class);

    @Mapping(target = "tests", ignore = true)
    @Mapping(target = "subjectTestGroupLinks", ignore = true)
    TestGroup toDomainEntity(TestGroupTable testGroupTable);

    @Mapping(target = "tests", ignore = true)
    @Mapping(target = "subjectTestGroupLinks", ignore = true)
    @Mapping(target = "Id", source = "id", qualifiedByName = "toUuid")
    TestGroupTable toTableEntity(TestGroup testGroup);
}

