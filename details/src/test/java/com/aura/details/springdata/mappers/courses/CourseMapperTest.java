package com.aura.details.springdata.mappers.courses;

import com.aura.core.entities.courses.Course;
import com.aura.details.springdata.models.courses.CourseModel;
import com.aura.details.springdata.models.levels.LevelModel;
import com.aura.details.springdata.models.outlets.OutletModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CourseMapper")
class CourseMapperTest {

    private final CourseMapper mapper = CourseMapper.INSTANCE;

    @DisplayName("Should map CourseModel.outlet.id into Course.outletId without traversing back into Outlet")
    @Test
    void toDomainEntity_extractsOutletIdFromModel() {
        UUID outletId = UUID.randomUUID();
        OutletModel outletModel = OutletModel.builder().id(outletId).build();
        CourseModel model = CourseModel.builder()
                .id(UUID.randomUUID())
                .name("Maths 101")
                .outlet(outletModel)
                .build();

        Course domain = mapper.toDomainEntity(model);

        assertThat(domain.getOutletId()).isEqualTo(outletId.toString());
        assertThat(domain.getName()).isEqualTo("Maths 101");
    }

    @DisplayName("Should map CourseModel.level.id into Course.levelId without traversing back into Level")
    @Test
    void toDomainEntity_extractsLevelIdFromModel() {
        UUID levelId = UUID.randomUUID();
        LevelModel levelModel = LevelModel.builder().id(levelId).build();
        CourseModel model = CourseModel.builder()
                .id(UUID.randomUUID())
                .name("Maths 101")
                .level(levelModel)
                .build();

        Course domain = mapper.toDomainEntity(model);

        assertThat(domain.getLevelId()).isEqualTo(levelId.toString());
        assertThat(domain.getName()).isEqualTo("Maths 101");
    }

    @DisplayName("Should leave CourseModel.outlet null on toModelEntity — CourseRepositoryImpl attaches via Refs.ref")
    @Test
    void toModelEntity_leavesOutletNullForRepositoryImplToAttach() {
        Course domain = Course.builder()
                .id(UUID.randomUUID().toString())
                .outletId(UUID.randomUUID().toString())
                .name("Maths 101")
                .build();

        CourseModel model = mapper.toModelEntity(domain);

        assertThat(model.getOutlet()).isNull();
        assertThat(model.getName()).isEqualTo("Maths 101");
    }

    @DisplayName("Should leave CourseModel.level null on toModelEntity — CourseRepositoryImpl attaches via Refs.ref")
    @Test
    void toModelEntity_leavesLevelNullForRepositoryImplToAttach() {
        Course domain = Course.builder()
                .id(UUID.randomUUID().toString())
                .levelId(UUID.randomUUID().toString())
                .name("Maths 101")
                .build();

        CourseModel model = mapper.toModelEntity(domain);

        assertThat(model.getLevel()).isNull();
        assertThat(model.getName()).isEqualTo("Maths 101");
    }

    @DisplayName("Should preserve non-association fields across a Model→Domain→Model round-trip")
    @Test
    void roundTrip_preservesNonAssociationFields() {
        UUID id = UUID.randomUUID();
        CourseModel original = CourseModel.builder()
                .id(id)
                .name("Maths 101")
                .build();

        CourseModel roundTripped = mapper.toModelEntity(mapper.toDomainEntity(original));

        assertThat(roundTripped.getId()).isEqualTo(id);
        assertThat(roundTripped.getName()).isEqualTo("Maths 101");
    }
}
