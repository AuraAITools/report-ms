package com.reportai.www.reportapi.entities.attachments;

import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.entities.Subject;
import com.reportai.www.reportapi.entities.base.AttachmentTenantAwareBaseEntityTemplate;
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
@Table(name = "SubjectStudentAttachments",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_subject_student",
                        columnNames = {"subject_id", "student_id"}
                )
        })
public class SubjectStudentAttachment extends AttachmentTenantAwareBaseEntityTemplate<Subject, Student, SubjectStudentAttachment> {

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @Override
    public Subject getFirstEntity() {
        return subject;
    }

    @Override
    public Student getSecondEntity() {
        return student;
    }

    @Override
    public Collection<SubjectStudentAttachment> getFirstEntityAttachments() {
        return subject != null ? subject.getSubjectStudentAttachments() : null;
    }

    @Override
    public Collection<SubjectStudentAttachment> getSecondEntityAttachments() {
        return student != null ? student.getSubjectStudentAttachments() : null;
    }

    @Override
    public SubjectStudentAttachment create(Subject entity1, Student entity2) {
        return
                SubjectStudentAttachment
                        .builder()
                        .subject(entity1)
                        .student(student)
                        .build();
    }
}
