package com.reportai.www.reportapi.entities.attachments;

import com.reportai.www.reportapi.entities.Subject;
import com.reportai.www.reportapi.entities.TestGroup;
import com.reportai.www.reportapi.entities.base.AttachmentTenantAwareBaseEntityTemplate;
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
@Table(name = "SubjectTestGroupAttachments",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_subject_test_group",
                        columnNames = {"subject_id", "test_group_id"}
                )
        })
public class SubjectTestGroupAttachment extends AttachmentTenantAwareBaseEntityTemplate<Subject, TestGroup, SubjectTestGroupAttachment> {
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "test_group_id")
    private TestGroup testGroup;

    @Override
    public Subject getFirstEntity() {
        return subject;
    }

    @Override
    public TestGroup getSecondEntity() {
        return testGroup;
    }

    @Override
    public Collection<SubjectTestGroupAttachment> getFirstEntityAttachments() {
        return subject != null ? subject.getSubjectTestGroupAttachments() : null;
    }

    @Override
    public Collection<SubjectTestGroupAttachment> getSecondEntityAttachments() {
        return testGroup != null ? testGroup.getSubjectTestGroupAttachments() : null;
    }

    @Override
    public SubjectTestGroupAttachment create(Subject entity1, TestGroup entity2) {
        return SubjectTestGroupAttachment
                .builder()
                .subject(entity1)
                .testGroup(entity2)
                .build();
    }
}
