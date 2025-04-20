package com.reportai.www.reportapi.entities.attachments;

import com.reportai.www.reportapi.entities.Subject;
import com.reportai.www.reportapi.entities.base.AttachmentTenantAwareBaseEntityTemplate;
import com.reportai.www.reportapi.entities.educators.Educator;
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
@Table(name = "SubjectEducatorAttachments",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_subject_educator",
                        columnNames = {"subject_id", "educator_id"}
                )
        })
public class SubjectEducatorAttachment extends AttachmentTenantAwareBaseEntityTemplate<Subject, Educator, SubjectEducatorAttachment> {

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "educator_id")
    private Educator educator;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @Override
    public Subject getFirstEntity() {
        return subject;
    }

    @Override
    public Educator getSecondEntity() {
        return educator;
    }

    @Override
    public Collection<SubjectEducatorAttachment> getFirstEntityAttachments() {
        return subject != null ? subject.getSubjectEducatorAttachments() : null;
    }

    @Override
    public Collection<SubjectEducatorAttachment> getSecondEntityAttachments() {
        return educator != null ? educator.getSubjectEducatorAttachments() : null;
    }

    @Override
    public SubjectEducatorAttachment create(Subject entity1, Educator entity2) {
        return SubjectEducatorAttachment
                .builder()
                .subject(entity1)
                .educator(entity2)
                .build();
    }
}
