package com.aura.details.springdata.mappers.tests;

import com.aura.core.entities.tests.TestGroup;
import com.aura.details.springdata.models.tests.TestGroupModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TestGroupMapper {
    TestGroupMapper INSTANCE = Mappers.getMapper(TestGroupMapper.class);

    TestGroup toDomainEntity(TestGroupModel testGroupModel);

    TestGroupModel toModelEntity(TestGroup testGroup);
}
