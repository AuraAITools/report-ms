package com.aura.details.springdata.mappers.tests;

import com.aura.core.entities.tests.TestResult;
import com.aura.details.springdata.mappers.students.StudentMapper;
import com.aura.details.springdata.mappers.subjects.SubjectMapper;
import com.aura.details.springdata.tables.tests.TestResultTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {TestMapper.class, StudentMapper.class, SubjectMapper.class})
public interface TestResultMapper {
    TestResultMapper INSTANCE = Mappers.getMapper(TestResultMapper.class);

    TestResult toDomainEntity(TestResultTable testResultTable);

    @Mapping(target = "Id", source = "id")
    TestResultTable toTableEntity(TestResult testResult);
}

