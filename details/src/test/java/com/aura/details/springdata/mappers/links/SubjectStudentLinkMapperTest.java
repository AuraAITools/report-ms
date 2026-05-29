package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.SubjectStudentLink;
import com.aura.details.springdata.models.links.SubjectStudentLinkModel;
import com.aura.details.springdata.models.students.StudentModel;
import com.aura.details.springdata.models.subjects.SubjectModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SubjectStudentLinkMapper")
class SubjectStudentLinkMapperTest {

    private final SubjectStudentLinkMapper mapper = SubjectStudentLinkMapper.INSTANCE;

    @DisplayName("Should map SubjectStudentLinkModel.student.id into SubjectStudentLink.studentId without traversing back into Student")
    @Test
    void toDomain_extractsStudentIdFromModel() {
        UUID studentId = UUID.randomUUID();
        StudentModel studentModel = StudentModel.builder().id(studentId).build();
        SubjectStudentLinkModel model = SubjectStudentLinkModel.builder()
                .id(UUID.randomUUID())
                .student(studentModel)
                .build();

        SubjectStudentLink domain = mapper.toDomain(model);

        assertThat(domain.getStudentId()).isEqualTo(studentId.toString());
    }

    @DisplayName("Should map SubjectStudentLinkModel.subject.id into SubjectStudentLink.subjectId without traversing back into Subject")
    @Test
    void toDomain_extractsSubjectIdFromModel() {
        UUID subjectId = UUID.randomUUID();
        SubjectModel subjectModel = SubjectModel.builder().id(subjectId).build();
        SubjectStudentLinkModel model = SubjectStudentLinkModel.builder()
                .id(UUID.randomUUID())
                .subject(subjectModel)
                .build();

        SubjectStudentLink domain = mapper.toDomain(model);

        assertThat(domain.getSubjectId()).isEqualTo(subjectId.toString());
    }

    @DisplayName("Should set SubjectStudentLinkModel.student to a stub StudentModel populated only with the id from SubjectStudentLink.studentId")
    @Test
    void toTable_buildsStudentStubFromStudentId() {
        UUID studentId = UUID.randomUUID();
        SubjectStudentLink domain = SubjectStudentLink.builder()
                .id(UUID.randomUUID().toString())
                .studentId(studentId.toString())
                .build();

        SubjectStudentLinkModel model = mapper.toModelEntity(domain);

        assertThat(model.getStudent()).isNotNull();
        assertThat(model.getStudent().getId()).isEqualTo(studentId);
        assertThat(model.getStudent().getName()).isNull();
    }

    @DisplayName("Should set SubjectStudentLinkModel.subject to a stub SubjectModel populated only with the id from SubjectStudentLink.subjectId")
    @Test
    void toTable_buildsSubjectStubFromSubjectId() {
        UUID subjectId = UUID.randomUUID();
        SubjectStudentLink domain = SubjectStudentLink.builder()
                .id(UUID.randomUUID().toString())
                .subjectId(subjectId.toString())
                .build();

        SubjectStudentLinkModel model = mapper.toModelEntity(domain);

        assertThat(model.getSubject()).isNotNull();
        assertThat(model.getSubject().getId()).isEqualTo(subjectId);
        assertThat(model.getSubject().getName()).isNull();
    }

    @DisplayName("Should leave SubjectStudentLinkModel.student null when SubjectStudentLink.studentId is null")
    @Test
    void toTable_returnsNullStudentWhenStudentIdNull() {
        SubjectStudentLink domain = SubjectStudentLink.builder()
                .id(UUID.randomUUID().toString())
                .studentId(null)
                .build();

        SubjectStudentLinkModel model = mapper.toModelEntity(domain);

        assertThat(model.getStudent()).isNull();
    }

    @DisplayName("Should leave SubjectStudentLinkModel.subject null when SubjectStudentLink.subjectId is null")
    @Test
    void toTable_returnsNullSubjectWhenSubjectIdNull() {
        SubjectStudentLink domain = SubjectStudentLink.builder()
                .id(UUID.randomUUID().toString())
                .subjectId(null)
                .build();

        SubjectStudentLinkModel model = mapper.toModelEntity(domain);

        assertThat(model.getSubject()).isNull();
    }

    @DisplayName("Should preserve studentId and subjectId across a Model→Domain→Model round-trip")
    @Test
    void roundTrip_preservesStudentIdAndSubjectId() {
        UUID studentId = UUID.randomUUID();
        UUID subjectId = UUID.randomUUID();
        StudentModel studentModel = StudentModel.builder().id(studentId).build();
        SubjectModel subjectModel = SubjectModel.builder().id(subjectId).build();
        SubjectStudentLinkModel original = SubjectStudentLinkModel.builder()
                .id(UUID.randomUUID())
                .student(studentModel)
                .subject(subjectModel)
                .build();

        SubjectStudentLinkModel roundTripped = mapper.toModelEntity(mapper.toDomain(original));

        assertThat(roundTripped.getStudent().getId()).isEqualTo(studentId);
        assertThat(roundTripped.getSubject().getId()).isEqualTo(subjectId);
    }
}
