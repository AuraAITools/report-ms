package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.StudentCourseLink;
import com.aura.details.springdata.models.courses.CourseModel;
import com.aura.details.springdata.models.links.StudentCourseLinkModel;
import com.aura.details.springdata.models.students.StudentModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("StudentCourseLinkMapper")
class StudentCourseLinkMapperTest {

    private final StudentCourseLinkMapper mapper = StudentCourseLinkMapper.INSTANCE;

    @DisplayName("Should map StudentCourseLinkModel.student.id into StudentCourseLink.studentId without traversing back into Student")
    @Test
    void toDomain_extractsStudentIdFromModel() {
        UUID studentId = UUID.randomUUID();
        StudentModel studentModel = StudentModel.builder().id(studentId).build();
        StudentCourseLinkModel model = StudentCourseLinkModel.builder()
                .id(UUID.randomUUID())
                .student(studentModel)
                .build();

        StudentCourseLink domain = mapper.toDomain(model);

        assertThat(domain.getStudentId()).isEqualTo(studentId.toString());
    }

    @DisplayName("Should map StudentCourseLinkModel.course.id into StudentCourseLink.courseId without traversing back into Course")
    @Test
    void toDomain_extractsCourseIdFromModel() {
        UUID courseId = UUID.randomUUID();
        CourseModel courseModel = CourseModel.builder().id(courseId).build();
        StudentCourseLinkModel model = StudentCourseLinkModel.builder()
                .id(UUID.randomUUID())
                .course(courseModel)
                .build();

        StudentCourseLink domain = mapper.toDomain(model);

        assertThat(domain.getCourseId()).isEqualTo(courseId.toString());
    }

    @DisplayName("Should leave StudentCourseLinkModel.student null on toTable — repository impl attaches via Refs.ref")
    @Test
    void toTable_leavesStudentNullForRepositoryImplToAttach() {
        StudentCourseLink domain = StudentCourseLink.builder()
                .id(UUID.randomUUID().toString())
                .studentId(UUID.randomUUID().toString())
                .build();

        StudentCourseLinkModel model = mapper.toTable(domain);

        assertThat(model.getStudent()).isNull();
    }

    @DisplayName("Should leave StudentCourseLinkModel.course null on toTable — repository impl attaches via Refs.ref")
    @Test
    void toTable_leavesCourseNullForRepositoryImplToAttach() {
        StudentCourseLink domain = StudentCourseLink.builder()
                .id(UUID.randomUUID().toString())
                .courseId(UUID.randomUUID().toString())
                .build();

        StudentCourseLinkModel model = mapper.toTable(domain);

        assertThat(model.getCourse()).isNull();
    }
}
