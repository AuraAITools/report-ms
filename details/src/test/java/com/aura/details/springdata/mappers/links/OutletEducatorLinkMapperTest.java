package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.OutletEducatorLink;
import com.aura.details.springdata.models.educators.EducatorModel;
import com.aura.details.springdata.models.links.OutletEducatorLinkModel;
import com.aura.details.springdata.models.outlets.OutletModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("OutletEducatorLinkMapper")
class OutletEducatorLinkMapperTest {

    private final OutletEducatorLinkMapper mapper = OutletEducatorLinkMapper.INSTANCE;

    @DisplayName("Should map OutletEducatorLinkModel.educator.id into OutletEducatorLink.educatorId without traversing back into Educator")
    @Test
    void toDomain_extractsEducatorIdFromModel() {
        UUID educatorId = UUID.randomUUID();
        EducatorModel educatorModel = EducatorModel.builder().id(educatorId).build();
        OutletEducatorLinkModel model = OutletEducatorLinkModel.builder()
                .id(UUID.randomUUID())
                .educator(educatorModel)
                .build();

        OutletEducatorLink domain = mapper.toDomain(model);

        assertThat(domain.getEducatorId()).isEqualTo(educatorId.toString());
    }

    @DisplayName("Should map OutletEducatorLinkModel.outlet.id into OutletEducatorLink.outletId without traversing back into Outlet")
    @Test
    void toDomain_extractsOutletIdFromModel() {
        UUID outletId = UUID.randomUUID();
        OutletModel outletModel = OutletModel.builder().id(outletId).build();
        OutletEducatorLinkModel model = OutletEducatorLinkModel.builder()
                .id(UUID.randomUUID())
                .outlet(outletModel)
                .build();

        OutletEducatorLink domain = mapper.toDomain(model);

        assertThat(domain.getOutletId()).isEqualTo(outletId.toString());
    }

    @DisplayName("Should set OutletEducatorLinkModel.educator to a stub EducatorModel populated only with the id from OutletEducatorLink.educatorId")
    @Test
    void toTable_buildsEducatorStubFromEducatorId() {
        UUID educatorId = UUID.randomUUID();
        OutletEducatorLink domain = OutletEducatorLink.builder()
                .id(UUID.randomUUID().toString())
                .educatorId(educatorId.toString())
                .build();

        OutletEducatorLinkModel model = mapper.toTable(domain);

        assertThat(model.getEducator()).isNotNull();
        assertThat(model.getEducator().getId()).isEqualTo(educatorId);
        assertThat(model.getEducator().getName()).isNull();
    }

    @DisplayName("Should set OutletEducatorLinkModel.outlet to a stub OutletModel populated only with the id from OutletEducatorLink.outletId")
    @Test
    void toTable_buildsOutletStubFromOutletId() {
        UUID outletId = UUID.randomUUID();
        OutletEducatorLink domain = OutletEducatorLink.builder()
                .id(UUID.randomUUID().toString())
                .outletId(outletId.toString())
                .build();

        OutletEducatorLinkModel model = mapper.toTable(domain);

        assertThat(model.getOutlet()).isNotNull();
        assertThat(model.getOutlet().getId()).isEqualTo(outletId);
        assertThat(model.getOutlet().getName()).isNull();
    }

    @DisplayName("Should leave OutletEducatorLinkModel.educator null when OutletEducatorLink.educatorId is null")
    @Test
    void toTable_returnsNullEducatorWhenEducatorIdNull() {
        OutletEducatorLink domain = OutletEducatorLink.builder()
                .id(UUID.randomUUID().toString())
                .educatorId(null)
                .build();

        OutletEducatorLinkModel model = mapper.toTable(domain);

        assertThat(model.getEducator()).isNull();
    }

    @DisplayName("Should leave OutletEducatorLinkModel.outlet null when OutletEducatorLink.outletId is null")
    @Test
    void toTable_returnsNullOutletWhenOutletIdNull() {
        OutletEducatorLink domain = OutletEducatorLink.builder()
                .id(UUID.randomUUID().toString())
                .outletId(null)
                .build();

        OutletEducatorLinkModel model = mapper.toTable(domain);

        assertThat(model.getOutlet()).isNull();
    }

    @DisplayName("Should preserve educatorId and outletId across a Model→Domain→Model round-trip")
    @Test
    void roundTrip_preservesEducatorIdAndOutletId() {
        UUID educatorId = UUID.randomUUID();
        UUID outletId = UUID.randomUUID();
        EducatorModel educatorModel = EducatorModel.builder().id(educatorId).build();
        OutletModel outletModel = OutletModel.builder().id(outletId).build();
        OutletEducatorLinkModel original = OutletEducatorLinkModel.builder()
                .id(UUID.randomUUID())
                .educator(educatorModel)
                .outlet(outletModel)
                .build();

        OutletEducatorLinkModel roundTripped = mapper.toTable(mapper.toDomain(original));

        assertThat(roundTripped.getEducator().getId()).isEqualTo(educatorId);
        assertThat(roundTripped.getOutlet().getId()).isEqualTo(outletId);
    }
}
