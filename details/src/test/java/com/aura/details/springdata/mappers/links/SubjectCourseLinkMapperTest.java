package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.SubjectCourseLink;
import com.aura.details.springdata.models.courses.CourseModel;
import com.aura.details.springdata.models.links.SubjectCourseLinkModel;
import com.aura.details.springdata.models.subjects.SubjectModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SubjectCourseLinkMapper")
class SubjectCourseLinkMapperTest {

    private final SubjectCourseLinkMapper mapper = SubjectCourseLinkMapper.INSTANCE;

    @DisplayName("Should map SubjectCourseLinkModel.subject.id into SubjectCourseLink.subjectId without traversing back into Subject")
    @Test
    void toDomain_extractsSubjectIdFromModel() {
        UUID subjectId = UUID.randomUUID();
        SubjectModel subjectModel = SubjectModel.builder().id(subjectId).build();
        SubjectCourseLinkModel model = SubjectCourseLinkModel.builder()
                .id(UUID.randomUUID())
                .subject(subjectModel)
                .build();

        SubjectCourseLink domain = mapper.toDomain(model);

        assertThat(domain.getSubjectId()).isEqualTo(subjectId.toString());
    }

    @DisplayName("Should map SubjectCourseLinkModel.course.id into SubjectCourseLink.courseId without traversing back into Course")
    @Test
    void toDomain_extractsCourseIdFromModel() {
        UUID courseId = UUID.randomUUID();
        CourseModel courseModel = CourseModel.builder().id(courseId).build();
        SubjectCourseLinkModel model = SubjectCourseLinkModel.builder()
                .id(UUID.randomUUID())
                .course(courseModel)
                .build();

        SubjectCourseLink domain = mapper.toDomain(model);

        assertThat(domain.getCourseId()).isEqualTo(courseId.toString());
    }

    @DisplayName("Should leave SubjectCourseLinkModel.subject null on toTable — repository impl attaches via Refs.ref")
    @Test
    void toTable_leavesSubjectNullForRepositoryImplToAttach() {
        SubjectCourseLink domain = SubjectCourseLink.builder()
                .id(UUID.randomUUID().toString())
                .subjectId(UUID.randomUUID().toString())
                .build();

        SubjectCourseLinkModel model = mapper.toTable(domain);

        assertThat(model.getSubject()).isNull();
    }

    @DisplayName("Should leave SubjectCourseLinkModel.course null on toTable — repository impl attaches via Refs.ref")
    @Test
    void toTable_leavesCourseNullForRepositoryImplToAttach() {
        SubjectCourseLink domain = SubjectCourseLink.builder()
                .id(UUID.randomUUID().toString())
                .courseId(UUID.randomUUID().toString())
                .build();

        SubjectCourseLinkModel model = mapper.toTable(domain);

        assertThat(model.getCourse()).isNull();
    }
}
