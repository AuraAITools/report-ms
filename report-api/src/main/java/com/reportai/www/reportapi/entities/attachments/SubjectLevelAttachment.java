package com.reportai.www.reportapi.entities.attachments;

import com.reportai.www.reportapi.entities.Level;
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
@Table(name = "SubjectLevelAttachments",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_subject_level",
                        columnNames = {"subject_id", "level_id"}
                )
        })
public class SubjectLevelAttachment extends AttachmentTenantAwareBaseEntityTemplate<Subject, Level, SubjectLevelAttachment> {

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "level_id")
    private Level level;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @Override
    public Subject getFirstEntity() {
        return subject;
    }

    @Override
    public Level getSecondEntity() {
        return level;
    }

    @Override
    public Collection<SubjectLevelAttachment> getFirstEntityAttachments() {
        return subject != null ? subject.getSubjectLevelAttachments() : null;
    }

    @Override
    public Collection<SubjectLevelAttachment> getSecondEntityAttachments() {
        return level != null ? subject.getSubjectLevelAttachments() : null;
    }

    @Override
    public SubjectLevelAttachment create(Subject entity1, Level entity2) {
        return SubjectLevelAttachment
                .builder()
                .subject(entity1)
                .level(entity2)
                .build();
    }
}
