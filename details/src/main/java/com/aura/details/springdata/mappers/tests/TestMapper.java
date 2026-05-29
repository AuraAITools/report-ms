package com.aura.details.springdata.mappers.tests;

import com.aura.core.entities.tests.Test;
import com.aura.details.springdata.models.tests.TestGroupModel;
import com.aura.details.springdata.models.tests.TestModel;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TestMapper {
    TestMapper INSTANCE = Mappers.getMapper(TestMapper.class);

    @Mapping(source = "testGroup.id", target = "testGroupId")
    Test toDomainEntity(TestModel testModel);

    @Mapping(source = "testGroupId", target = "testGroup", qualifiedByName = "testGroupRef")
    TestModel toModelEntity(Test test);

    @Named("testGroupRef")
    default TestGroupModel testGroupRef(String testGroupId) {
        if (testGroupId == null) {
            return null;
        }
        return TestGroupModel.builder().id(UUID.fromString(testGroupId)).build();
    }
}
