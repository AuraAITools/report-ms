package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.StudentOutletLink;
import com.aura.details.springdata.models.links.StudentOutletLinkModel;
import com.aura.details.springdata.models.outlets.OutletModel;
import com.aura.details.springdata.models.students.StudentModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("StudentOutletLinkMapper")
class StudentOutletLinkMapperTest {

    private final StudentOutletLinkMapper mapper = StudentOutletLinkMapper.INSTANCE;

    @DisplayName("Should map StudentOutletLinkModel.student.id into StudentOutletLink.studentId without traversing back into Student")
    @Test
    void toDomain_extractsStudentIdFromModel() {
        UUID studentId = UUID.randomUUID();
        StudentModel studentModel = StudentModel.builder().id(studentId).build();
        StudentOutletLinkModel model = StudentOutletLinkModel.builder()
                .id(UUID.randomUUID())
                .student(studentModel)
                .build();

        StudentOutletLink domain = mapper.toDomain(model);

        assertThat(domain.getStudentId()).isEqualTo(studentId.toString());
    }

    @DisplayName("Should map StudentOutletLinkModel.outlet.id into StudentOutletLink.outletId without traversing back into Outlet")
    @Test
    void toDomain_extractsOutletIdFromModel() {
        UUID outletId = UUID.randomUUID();
        OutletModel outletModel = OutletModel.builder().id(outletId).build();
        StudentOutletLinkModel model = StudentOutletLinkModel.builder()
                .id(UUID.randomUUID())
                .outlet(outletModel)
                .build();

        StudentOutletLink domain = mapper.toDomain(model);

        assertThat(domain.getOutletId()).isEqualTo(outletId.toString());
    }

    @DisplayName("Should set StudentOutletLinkModel.student to a stub StudentModel populated only with the id from StudentOutletLink.studentId")
    @Test
    void toTable_buildsStudentStubFromStudentId() {
        UUID studentId = UUID.randomUUID();
        StudentOutletLink domain = StudentOutletLink.builder()
                .id(UUID.randomUUID().toString())
                .studentId(studentId.toString())
                .build();

        StudentOutletLinkModel model = mapper.toTable(domain);

        assertThat(model.getStudent()).isNotNull();
        assertThat(model.getStudent().getId()).isEqualTo(studentId);
        assertThat(model.getStudent().getName()).isNull();
    }

    @DisplayName("Should set StudentOutletLinkModel.outlet to a stub OutletModel populated only with the id from StudentOutletLink.outletId")
    @Test
    void toTable_buildsOutletStubFromOutletId() {
        UUID outletId = UUID.randomUUID();
        StudentOutletLink domain = StudentOutletLink.builder()
                .id(UUID.randomUUID().toString())
                .outletId(outletId.toString())
                .build();

        StudentOutletLinkModel model = mapper.toTable(domain);

        assertThat(model.getOutlet()).isNotNull();
        assertThat(model.getOutlet().getId()).isEqualTo(outletId);
        assertThat(model.getOutlet().getName()).isNull();
    }

    @DisplayName("Should leave StudentOutletLinkModel.student null when StudentOutletLink.studentId is null")
    @Test
    void toTable_returnsNullStudentWhenStudentIdNull() {
        StudentOutletLink domain = StudentOutletLink.builder()
                .id(UUID.randomUUID().toString())
                .studentId(null)
                .build();

        StudentOutletLinkModel model = mapper.toTable(domain);

        assertThat(model.getStudent()).isNull();
    }

    @DisplayName("Should leave StudentOutletLinkModel.outlet null when StudentOutletLink.outletId is null")
    @Test
    void toTable_returnsNullOutletWhenOutletIdNull() {
        StudentOutletLink domain = StudentOutletLink.builder()
                .id(UUID.randomUUID().toString())
                .outletId(null)
                .build();

        StudentOutletLinkModel model = mapper.toTable(domain);

        assertThat(model.getOutlet()).isNull();
    }

    @DisplayName("Should preserve studentId and outletId across a Model→Domain→Model round-trip")
    @Test
    void roundTrip_preservesStudentIdAndOutletId() {
        UUID studentId = UUID.randomUUID();
        UUID outletId = UUID.randomUUID();
        StudentModel studentModel = StudentModel.builder().id(studentId).build();
        OutletModel outletModel = OutletModel.builder().id(outletId).build();
        StudentOutletLinkModel original = StudentOutletLinkModel.builder()
                .id(UUID.randomUUID())
                .student(studentModel)
                .outlet(outletModel)
                .build();

        StudentOutletLinkModel roundTripped = mapper.toTable(mapper.toDomain(original));

        assertThat(roundTripped.getStudent().getId()).isEqualTo(studentId);
        assertThat(roundTripped.getOutlet().getId()).isEqualTo(outletId);
    }
}
