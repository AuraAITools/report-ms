package com.aura.details.mappers.tests;

import com.aura.core.entities.tests.TestResult;
import com.aura.details.mappers.students.StudentMapper;
import com.aura.details.mappers.subjects.SubjectMapper;
import com.aura.details.tables.tests.TestResultTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {TestMapper.class, StudentMapper.class, SubjectMapper.class})
public interface TestResultMapper {
    TestResultMapper INSTANCE = Mappers.getMapper(TestResultMapper.class);

    TestResult toDomainEntity(TestResultTable testResultTable);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "Id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    TestResultTable toTableEntity(TestResult testResult);
}

