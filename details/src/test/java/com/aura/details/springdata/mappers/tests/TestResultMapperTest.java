package com.aura.details.springdata.mappers.tests;

import com.aura.core.entities.tests.TestResult;
import com.aura.details.springdata.models.students.StudentModel;
import com.aura.details.springdata.models.subjects.SubjectModel;
import com.aura.details.springdata.models.tests.TestModel;
import com.aura.details.springdata.models.tests.TestResultModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TestResultMapper")
class TestResultMapperTest {

    private final TestResultMapper mapper = TestResultMapper.INSTANCE;

    @DisplayName("Should map TestResultModel.test.id into TestResult.testId without traversing back into Test")
    @Test
    void toDomainEntity_extractsTestIdFromModel() {
        UUID testId = UUID.randomUUID();
        TestModel testModel = TestModel.builder().id(testId).build();
        TestResultModel model = TestResultModel.builder()
                .id(UUID.randomUUID())
                .test(testModel)
                .build();

        TestResult domain = mapper.toDomainEntity(model);

        assertThat(domain.getTestId()).isEqualTo(testId.toString());
    }

    @DisplayName("Should map TestResultModel.student.id into TestResult.studentId without traversing back into Student")
    @Test
    void toDomainEntity_extractsStudentIdFromModel() {
        UUID studentId = UUID.randomUUID();
        StudentModel studentModel = StudentModel.builder().id(studentId).build();
        TestResultModel model = TestResultModel.builder()
                .id(UUID.randomUUID())
                .student(studentModel)
                .build();

        TestResult domain = mapper.toDomainEntity(model);

        assertThat(domain.getStudentId()).isEqualTo(studentId.toString());
    }

    @DisplayName("Should map TestResultModel.subject.id into TestResult.subjectId without traversing back into Subject")
    @Test
    void toDomainEntity_extractsSubjectIdFromModel() {
        UUID subjectId = UUID.randomUUID();
        SubjectModel subjectModel = SubjectModel.builder().id(subjectId).build();
        TestResultModel model = TestResultModel.builder()
                .id(UUID.randomUUID())
                .subject(subjectModel)
                .build();

        TestResult domain = mapper.toDomainEntity(model);

        assertThat(domain.getSubjectId()).isEqualTo(subjectId.toString());
    }

    @DisplayName("Should set TestResultModel.test to a stub TestModel populated only with the id from TestResult.testId")
    @Test
    void toModelEntity_buildsTestStubFromTestId() {
        UUID testId = UUID.randomUUID();
        TestResult domain = TestResult.builder()
                .id(UUID.randomUUID().toString())
                .testId(testId.toString())
                .build();

        TestResultModel model = mapper.toModelEntity(domain);

        assertThat(model.getTest()).isNotNull();
        assertThat(model.getTest().getId()).isEqualTo(testId);
        assertThat(model.getTest().getName()).isNull();
    }

    @DisplayName("Should set TestResultModel.student to a stub StudentModel populated only with the id from TestResult.studentId")
    @Test
    void toModelEntity_buildsStudentStubFromStudentId() {
        UUID studentId = UUID.randomUUID();
        TestResult domain = TestResult.builder()
                .id(UUID.randomUUID().toString())
                .studentId(studentId.toString())
                .build();

        TestResultModel model = mapper.toModelEntity(domain);

        assertThat(model.getStudent()).isNotNull();
        assertThat(model.getStudent().getId()).isEqualTo(studentId);
        assertThat(model.getStudent().getName()).isNull();
    }

    @DisplayName("Should set TestResultModel.subject to a stub SubjectModel populated only with the id from TestResult.subjectId")
    @Test
    void toModelEntity_buildsSubjectStubFromSubjectId() {
        UUID subjectId = UUID.randomUUID();
        TestResult domain = TestResult.builder()
                .id(UUID.randomUUID().toString())
                .subjectId(subjectId.toString())
                .build();

        TestResultModel model = mapper.toModelEntity(domain);

        assertThat(model.getSubject()).isNotNull();
        assertThat(model.getSubject().getId()).isEqualTo(subjectId);
        assertThat(model.getSubject().getName()).isNull();
    }

    @DisplayName("Should leave TestResultModel.test null when TestResult.testId is null")
    @Test
    void toModelEntity_returnsNullTestWhenTestIdNull() {
        TestResult domain = TestResult.builder()
                .id(UUID.randomUUID().toString())
                .testId(null)
                .build();

        TestResultModel model = mapper.toModelEntity(domain);

        assertThat(model.getTest()).isNull();
    }

    @DisplayName("Should leave TestResultModel.student null when TestResult.studentId is null")
    @Test
    void toModelEntity_returnsNullStudentWhenStudentIdNull() {
        TestResult domain = TestResult.builder()
                .id(UUID.randomUUID().toString())
                .studentId(null)
                .build();

        TestResultModel model = mapper.toModelEntity(domain);

        assertThat(model.getStudent()).isNull();
    }

    @DisplayName("Should leave TestResultModel.subject null when TestResult.subjectId is null")
    @Test
    void toModelEntity_returnsNullSubjectWhenSubjectIdNull() {
        TestResult domain = TestResult.builder()
                .id(UUID.randomUUID().toString())
                .subjectId(null)
                .build();

        TestResultModel model = mapper.toModelEntity(domain);

        assertThat(model.getSubject()).isNull();
    }

    @DisplayName("Should preserve testId, studentId and subjectId across a Model->Domain->Model round-trip")
    @Test
    void roundTrip_preservesAllThreeIds() {
        UUID testId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        UUID subjectId = UUID.randomUUID();
        TestModel testModel = TestModel.builder().id(testId).build();
        StudentModel studentModel = StudentModel.builder().id(studentId).build();
        SubjectModel subjectModel = SubjectModel.builder().id(subjectId).build();
        TestResultModel original = TestResultModel.builder()
                .id(UUID.randomUUID())
                .test(testModel)
                .student(studentModel)
                .subject(subjectModel)
                .build();

        TestResultModel roundTripped = mapper.toModelEntity(mapper.toDomainEntity(original));

        assertThat(roundTripped.getTest().getId()).isEqualTo(testId);
        assertThat(roundTripped.getStudent().getId()).isEqualTo(studentId);
        assertThat(roundTripped.getSubject().getId()).isEqualTo(subjectId);
    }
}
