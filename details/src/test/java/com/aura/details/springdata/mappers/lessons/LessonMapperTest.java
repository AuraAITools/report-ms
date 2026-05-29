package com.aura.details.springdata.mappers.lessons;

import com.aura.core.entities.lessons.Lesson;
import com.aura.details.springdata.models.courses.CourseModel;
import com.aura.details.springdata.models.lessons.LessonModel;
import com.aura.details.springdata.models.outlets.OutletModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("LessonMapper")
class LessonMapperTest {

    private final LessonMapper mapper = LessonMapper.INSTANCE;

    @DisplayName("Should map LessonModel.course.id into Lesson.courseId without traversing back into Course")
    @Test
    void toDomainEntity_extractsCourseIdFromModel() {
        UUID courseId = UUID.randomUUID();
        CourseModel courseModel = CourseModel.builder().id(courseId).build();
        LessonModel model = LessonModel.builder()
                .id(UUID.randomUUID())
                .name("Algebra Lesson")
                .course(courseModel)
                .build();

        Lesson domain = mapper.toDomainEntity(model);

        assertThat(domain.getCourseId()).isEqualTo(courseId.toString());
        assertThat(domain.getName()).isEqualTo("Algebra Lesson");
    }

    @DisplayName("Should map LessonModel.outlet.id into Lesson.outletId without traversing back into Outlet")
    @Test
    void toDomainEntity_extractsOutletIdFromModel() {
        UUID outletId = UUID.randomUUID();
        OutletModel outletModel = OutletModel.builder().id(outletId).build();
        LessonModel model = LessonModel.builder()
                .id(UUID.randomUUID())
                .name("Algebra Lesson")
                .outlet(outletModel)
                .build();

        Lesson domain = mapper.toDomainEntity(model);

        assertThat(domain.getOutletId()).isEqualTo(outletId.toString());
        assertThat(domain.getName()).isEqualTo("Algebra Lesson");
    }

    @DisplayName("Should leave LessonModel.course null on toModelEntity — LessonRepositoryImpl attaches via Refs.ref")
    @Test
    void toModelEntity_leavesCourseNullForRepositoryImplToAttach() {
        Lesson domain = Lesson.builder()
                .id(UUID.randomUUID().toString())
                .courseId(UUID.randomUUID().toString())
                .name("Algebra Lesson")
                .build();

        LessonModel model = mapper.toModelEntity(domain);

        assertThat(model.getCourse()).isNull();
        assertThat(model.getName()).isEqualTo("Algebra Lesson");
    }

    @DisplayName("Should leave LessonModel.outlet null on toModelEntity — LessonRepositoryImpl attaches via Refs.ref")
    @Test
    void toModelEntity_leavesOutletNullForRepositoryImplToAttach() {
        Lesson domain = Lesson.builder()
                .id(UUID.randomUUID().toString())
                .outletId(UUID.randomUUID().toString())
                .name("Algebra Lesson")
                .build();

        LessonModel model = mapper.toModelEntity(domain);

        assertThat(model.getOutlet()).isNull();
        assertThat(model.getName()).isEqualTo("Algebra Lesson");
    }

    @DisplayName("Should preserve non-association fields across a Model→Domain→Model round-trip")
    @Test
    void roundTrip_preservesNonAssociationFields() {
        UUID id = UUID.randomUUID();
        LessonModel original = LessonModel.builder()
                .id(id)
                .name("Algebra Lesson")
                .build();

        LessonModel roundTripped = mapper.toModelEntity(mapper.toDomainEntity(original));

        assertThat(roundTripped.getId()).isEqualTo(id);
        assertThat(roundTripped.getName()).isEqualTo("Algebra Lesson");
    }
}
