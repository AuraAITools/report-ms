package com.aura.details.springdata.mappers.lessons;

import com.aura.core.entities.lessons.PriceRecord;
import com.aura.details.springdata.models.courses.CourseModel;
import com.aura.details.springdata.models.lessons.PriceRecordModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("PriceRecordMapper")
class PriceRecordMapperTest {

    private final PriceRecordMapper mapper = PriceRecordMapper.INSTANCE;

    @DisplayName("Should map PriceRecordModel into PriceRecord without populating the courseId back-reference")
    @Test
    void toDomainEntity_skipsCourseIdBackReference() {
        UUID courseId = UUID.randomUUID();
        CourseModel courseModel = CourseModel.builder().id(courseId).build();
        PriceRecordModel model = PriceRecordModel.builder()
                .id(UUID.randomUUID())
                .course(courseModel)
                .price(99.5)
                .frequency(PriceRecordModel.FREQUENCY.MONTHLY)
                .build();

        PriceRecord domain = mapper.toDomainEntity(model);

        assertThat(domain.getCourseId()).isNull();
        assertThat(domain.getPrice()).isEqualTo(99.5);
        assertThat(domain.getFrequency()).isEqualTo(PriceRecord.FREQUENCY.MONTHLY);
    }

    @DisplayName("Should leave PriceRecordModel.course null on toModelEntity — PriceRecordRepositoryImpl attaches via Refs.ref")
    @Test
    void toModelEntity_leavesCourseNullForRepositoryImplToAttach() {
        PriceRecord domain = PriceRecord.builder()
                .id(UUID.randomUUID().toString())
                .courseId(UUID.randomUUID().toString())
                .price(99.5)
                .frequency(PriceRecord.FREQUENCY.MONTHLY)
                .build();

        PriceRecordModel model = mapper.toModelEntity(domain);

        assertThat(model.getCourse()).isNull();
        assertThat(model.getPrice()).isEqualTo(99.5);
        assertThat(model.getFrequency()).isEqualTo(PriceRecordModel.FREQUENCY.MONTHLY);
    }

    @DisplayName("Should preserve non-association fields across a Model→Domain→Model round-trip")
    @Test
    void roundTrip_preservesNonAssociationFields() {
        PriceRecordModel original = PriceRecordModel.builder()
                .id(UUID.randomUUID())
                .price(99.5)
                .frequency(PriceRecordModel.FREQUENCY.MONTHLY)
                .build();

        PriceRecordModel roundTripped = mapper.toModelEntity(mapper.toDomainEntity(original));

        assertThat(roundTripped.getPrice()).isEqualTo(99.5);
        assertThat(roundTripped.getFrequency()).isEqualTo(PriceRecordModel.FREQUENCY.MONTHLY);
    }
}
