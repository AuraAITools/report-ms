package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.EducatorCourseLink;
import com.aura.details.springdata.models.courses.CourseModel;
import com.aura.details.springdata.models.educators.EducatorModel;
import com.aura.details.springdata.models.links.EducatorCourseLinkModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("EducatorCourseLinkMapper")
class EducatorCourseLinkMapperTest {

    private final EducatorCourseLinkMapper mapper = EducatorCourseLinkMapper.INSTANCE;

    @DisplayName("Should map EducatorCourseLinkModel.educator.id into EducatorCourseLink.educatorId without traversing back into Educator")
    @Test
    void toDomain_extractsEducatorIdFromModel() {
        UUID educatorId = UUID.randomUUID();
        EducatorModel educatorModel = EducatorModel.builder().id(educatorId).build();
        EducatorCourseLinkModel model = EducatorCourseLinkModel.builder()
                .id(UUID.randomUUID())
                .educator(educatorModel)
                .build();

        EducatorCourseLink domain = mapper.toDomain(model);

        assertThat(domain.getEducatorId()).isEqualTo(educatorId.toString());
    }

    @DisplayName("Should map EducatorCourseLinkModel.course.id into EducatorCourseLink.courseId without traversing back into Course")
    @Test
    void toDomain_extractsCourseIdFromModel() {
        UUID courseId = UUID.randomUUID();
        CourseModel courseModel = CourseModel.builder().id(courseId).build();
        EducatorCourseLinkModel model = EducatorCourseLinkModel.builder()
                .id(UUID.randomUUID())
                .course(courseModel)
                .build();

        EducatorCourseLink domain = mapper.toDomain(model);

        assertThat(domain.getCourseId()).isEqualTo(courseId.toString());
    }

    @DisplayName("Should leave EducatorCourseLinkModel.educator null on toTable — repository impl attaches via Refs.ref")
    @Test
    void toTable_leavesEducatorNullForRepositoryImplToAttach() {
        EducatorCourseLink domain = EducatorCourseLink.builder()
                .id(UUID.randomUUID().toString())
                .educatorId(UUID.randomUUID().toString())
                .build();

        EducatorCourseLinkModel model = mapper.toTable(domain);

        assertThat(model.getEducator()).isNull();
    }

    @DisplayName("Should leave EducatorCourseLinkModel.course null on toTable — repository impl attaches via Refs.ref")
    @Test
    void toTable_leavesCourseNullForRepositoryImplToAttach() {
        EducatorCourseLink domain = EducatorCourseLink.builder()
                .id(UUID.randomUUID().toString())
                .courseId(UUID.randomUUID().toString())
                .build();

        EducatorCourseLinkModel model = mapper.toTable(domain);

        assertThat(model.getCourse()).isNull();
    }
}
