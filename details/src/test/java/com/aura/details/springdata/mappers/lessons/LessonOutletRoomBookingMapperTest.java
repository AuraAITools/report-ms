package com.aura.details.springdata.mappers.lessons;

import com.aura.core.entities.lessons.LessonOutletRoomBooking;
import com.aura.details.springdata.models.lessons.LessonModel;
import com.aura.details.springdata.models.lessons.LessonOutletRoomBookingModel;
import com.aura.details.springdata.models.outlets.OutletRoomModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("LessonOutletRoomBookingMapper")
class LessonOutletRoomBookingMapperTest {

    private final LessonOutletRoomBookingMapper mapper = LessonOutletRoomBookingMapper.INSTANCE;

    @DisplayName("Should map LessonOutletRoomBookingModel.outletRoom.id into LessonOutletRoomBooking.outletRoomId without traversing back into OutletRoom")
    @Test
    void toDomainEntity_extractsOutletRoomIdFromModel() {
        UUID outletRoomId = UUID.randomUUID();
        OutletRoomModel outletRoomModel = OutletRoomModel.builder().id(outletRoomId).build();
        LessonOutletRoomBookingModel model = LessonOutletRoomBookingModel.builder()
                .id(UUID.randomUUID())
                .outletRoom(outletRoomModel)
                .build();

        LessonOutletRoomBooking domain = mapper.toDomainEntity(model);

        assertThat(domain.getOutletRoomId()).isEqualTo(outletRoomId.toString());
    }

    @DisplayName("Should map LessonOutletRoomBookingModel.lesson.id into LessonOutletRoomBooking.lessonId without traversing back into Lesson")
    @Test
    void toDomainEntity_extractsLessonIdFromModel() {
        UUID lessonId = UUID.randomUUID();
        LessonModel lessonModel = LessonModel.builder().id(lessonId).build();
        LessonOutletRoomBookingModel model = LessonOutletRoomBookingModel.builder()
                .id(UUID.randomUUID())
                .lesson(lessonModel)
                .build();

        LessonOutletRoomBooking domain = mapper.toDomainEntity(model);

        assertThat(domain.getLessonId()).isEqualTo(lessonId.toString());
    }

    @DisplayName("Should set LessonOutletRoomBookingModel.outletRoom to a stub OutletRoomModel populated only with the id from LessonOutletRoomBooking.outletRoomId")
    @Test
    void toModelEntity_buildsOutletRoomStubFromOutletRoomId() {
        UUID outletRoomId = UUID.randomUUID();
        LessonOutletRoomBooking domain = LessonOutletRoomBooking.builder()
                .id(UUID.randomUUID().toString())
                .outletRoomId(outletRoomId.toString())
                .build();

        LessonOutletRoomBookingModel model = mapper.toModelEntity(domain);

        assertThat(model.getOutletRoom()).isNotNull();
        assertThat(model.getOutletRoom().getId()).isEqualTo(outletRoomId);
        assertThat(model.getOutletRoom().getName()).isNull();
    }

    @DisplayName("Should set LessonOutletRoomBookingModel.lesson to a stub LessonModel populated only with the id from LessonOutletRoomBooking.lessonId")
    @Test
    void toModelEntity_buildsLessonStubFromLessonId() {
        UUID lessonId = UUID.randomUUID();
        LessonOutletRoomBooking domain = LessonOutletRoomBooking.builder()
                .id(UUID.randomUUID().toString())
                .lessonId(lessonId.toString())
                .build();

        LessonOutletRoomBookingModel model = mapper.toModelEntity(domain);

        assertThat(model.getLesson()).isNotNull();
        assertThat(model.getLesson().getId()).isEqualTo(lessonId);
        assertThat(model.getLesson().getName()).isNull();
    }

    @DisplayName("Should leave LessonOutletRoomBookingModel.outletRoom null when LessonOutletRoomBooking.outletRoomId is null")
    @Test
    void toModelEntity_returnsNullOutletRoomWhenOutletRoomIdNull() {
        LessonOutletRoomBooking domain = LessonOutletRoomBooking.builder()
                .id(UUID.randomUUID().toString())
                .outletRoomId(null)
                .build();

        LessonOutletRoomBookingModel model = mapper.toModelEntity(domain);

        assertThat(model.getOutletRoom()).isNull();
    }

    @DisplayName("Should leave LessonOutletRoomBookingModel.lesson null when LessonOutletRoomBooking.lessonId is null")
    @Test
    void toModelEntity_returnsNullLessonWhenLessonIdNull() {
        LessonOutletRoomBooking domain = LessonOutletRoomBooking.builder()
                .id(UUID.randomUUID().toString())
                .lessonId(null)
                .build();

        LessonOutletRoomBookingModel model = mapper.toModelEntity(domain);

        assertThat(model.getLesson()).isNull();
    }

    @DisplayName("Should preserve outletRoomId and lessonId across a Model→Domain→Model round-trip")
    @Test
    void roundTrip_preservesOutletRoomIdAndLessonId() {
        UUID outletRoomId = UUID.randomUUID();
        UUID lessonId = UUID.randomUUID();
        OutletRoomModel outletRoomModel = OutletRoomModel.builder().id(outletRoomId).build();
        LessonModel lessonModel = LessonModel.builder().id(lessonId).build();
        LessonOutletRoomBookingModel original = LessonOutletRoomBookingModel.builder()
                .id(UUID.randomUUID())
                .outletRoom(outletRoomModel)
                .lesson(lessonModel)
                .build();

        LessonOutletRoomBookingModel roundTripped = mapper.toModelEntity(mapper.toDomainEntity(original));

        assertThat(roundTripped.getOutletRoom().getId()).isEqualTo(outletRoomId);
        assertThat(roundTripped.getLesson().getId()).isEqualTo(lessonId);
    }
}
