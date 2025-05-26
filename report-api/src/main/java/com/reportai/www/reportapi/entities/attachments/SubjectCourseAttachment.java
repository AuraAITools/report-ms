package com.reportai.www.reportapi.entities.attachments;

import com.reportai.www.reportapi.entities.Subject;
import com.reportai.www.reportapi.entities.base.AttachmentTenantAwareBaseEntityTemplate;
import com.reportai.www.reportapi.entities.courses.Course;
import jakarta.persistence.Entity;
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
import org.hibernate.envers.Audited;

@Entity
@Audited
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SubjectCourseAttachments",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_subject_course",
                        columnNames = {"subject_id", "course_id"}
                )
        })
public class SubjectCourseAttachment extends AttachmentTenantAwareBaseEntityTemplate<Subject, Course> {
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "course_id")
    private Course course;

    @Override
    public Subject getFirstEntity() {
        return subject;
    }

    @Override
    public Course getSecondEntity() {
        return course;
    }

    @Override
    public Collection<SubjectCourseAttachment> getFirstEntityAttachments() {
        return subject != null ? subject.getSubjectCourseAttachments() : null;
    }

    @Override
    public Collection<SubjectCourseAttachment> getSecondEntityAttachments() {
        return course != null ? course.getSubjectCourseAttachments() : null;
    }

    @Override
    public SubjectCourseAttachment create(Subject entity1, Course entity2) {
        return SubjectCourseAttachment
                .builder()
                .subject(entity1)
                .course(entity2)
                .build();
    }
}
