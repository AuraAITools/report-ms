package com.reportai.www.reportapi.entities.attachments;

import com.reportai.www.reportapi.entities.base.AttachmentTenantAwareBaseEntityTemplate;
import com.reportai.www.reportapi.entities.courses.Course;
import com.reportai.www.reportapi.entities.educators.Educator;
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

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "EducatorCourseAttachments",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_educator_course",
                        columnNames = {"educator_id", "course_id"}
                )
        })
public class EducatorCourseAttachment extends AttachmentTenantAwareBaseEntityTemplate<Educator, Course, EducatorCourseAttachment> {
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "educator_id")
    private Educator educator;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "course_id")
    private Course course;

    @Override
    public Educator getFirstEntity() {
        return educator;
    }

    @Override
    public Course getSecondEntity() {
        return course;
    }

    @Override
    public Collection<EducatorCourseAttachment> getFirstEntityAttachments() {
        return educator != null ? educator.getEducatorCourseAttachments() : null;
    }

    @Override
    public Collection<EducatorCourseAttachment> getSecondEntityAttachments() {
        return course != null ? course.getEducatorCourseAttachments() : null;
    }

    @Override
    public EducatorCourseAttachment create(Educator entity1, Course entity2) {
        return EducatorCourseAttachment
                .builder()
                .educator(entity1)
                .course(entity2)
                .build();
    }


}
