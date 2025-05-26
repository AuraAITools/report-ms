package com.reportai.www.reportapi.entities.attachments;

import com.reportai.www.reportapi.entities.Level;
import com.reportai.www.reportapi.entities.base.AttachmentTenantAwareBaseEntityTemplate;
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
import org.hibernate.envers.Audited;

@Entity
@Audited
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "LevelEducatorAttachments",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_level_educator",
                        columnNames = {"level_id", "educator_id"}
                )
        })
public class LevelEducatorAttachment extends AttachmentTenantAwareBaseEntityTemplate<Level, Educator> {

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "educator_id")
    private Educator educator;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "level_id")
    private Level level;

    @Override
    public Level getFirstEntity() {
        return level;
    }

    @Override
    public Educator getSecondEntity() {
        return educator;
    }

    @Override
    public Collection<LevelEducatorAttachment> getFirstEntityAttachments() {
        return level != null ? level.getLevelEducatorAttachments() : null;
    }

    @Override
    public Collection<LevelEducatorAttachment> getSecondEntityAttachments() {
        return educator != null ? educator.getLevelEducatorAttachments() : null;
    }

    @Override
    public LevelEducatorAttachment create(Level entity1, Educator entity2) {
        return LevelEducatorAttachment
                .builder()
                .level(entity1)
                .educator(entity2)
                .build();
    }
}
