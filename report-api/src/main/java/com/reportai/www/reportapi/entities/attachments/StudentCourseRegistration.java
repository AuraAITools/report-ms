package com.reportai.www.reportapi.entities.attachments;

import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.entities.base.AttachmentTenantAwareBaseEntityTemplate;
import com.reportai.www.reportapi.entities.courses.Course;
import jakarta.persistence.Entity;
import org.hibernate.envers.Audited;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Audited
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "StudentCourseRegistrations",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_student_course",
                        columnNames = {"student_id", "course_id"}
                )
        })
public class StudentCourseRegistration extends AttachmentTenantAwareBaseEntityTemplate<Student, Course, StudentCourseRegistration> {
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "course_id")
    private Course course;

    @Override
    public Student getFirstEntity() {
        return student;
    }

    @Override
    public Course getSecondEntity() {
        return course;
    }

    @Override
    public Collection<StudentCourseRegistration> getFirstEntityAttachments() {
        return student != null ? student.getStudentCourseRegistrations() : null;
    }

    @Override
    public Collection<StudentCourseRegistration> getSecondEntityAttachments() {
        return course != null ? course.getStudentCourseRegistrations() : null;
    }

    @Override
    public StudentCourseRegistration create(Student entity1, Course entity2) {
        return StudentCourseRegistration
                .builder()
                .student(entity1)
                .course(entity2)
                .build();
    }
}
