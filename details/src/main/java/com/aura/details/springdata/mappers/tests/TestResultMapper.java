package com.aura.details.springdata.mappers.tests;

import com.aura.core.entities.tests.TestResult;
import com.aura.details.springdata.models.students.StudentModel;
import com.aura.details.springdata.models.subjects.SubjectModel;
import com.aura.details.springdata.models.tests.TestModel;
import com.aura.details.springdata.models.tests.TestResultModel;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TestResultMapper {
    TestResultMapper INSTANCE = Mappers.getMapper(TestResultMapper.class);

    @Mapping(source = "test.id", target = "testId")
    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "subject.id", target = "subjectId")
    TestResult toDomainEntity(TestResultModel model);

    @Mapping(source = "testId", target = "test", qualifiedByName = "testRef")
    @Mapping(source = "studentId", target = "student", qualifiedByName = "studentRef")
    @Mapping(source = "subjectId", target = "subject", qualifiedByName = "subjectRef")
    TestResultModel toModelEntity(TestResult testResult);

    @Named("testRef")
    default TestModel testRef(String testId) {
        if (testId == null) {
            return null;
        }
        return TestModel.builder().id(UUID.fromString(testId)).build();
    }

    @Named("studentRef")
    default StudentModel studentRef(String studentId) {
        if (studentId == null) {
            return null;
        }
        return StudentModel.builder().id(UUID.fromString(studentId)).build();
    }

    @Named("subjectRef")
    default SubjectModel subjectRef(String subjectId) {
        if (subjectId == null) {
            return null;
        }
        return SubjectModel.builder().id(UUID.fromString(subjectId)).build();
    }
}
