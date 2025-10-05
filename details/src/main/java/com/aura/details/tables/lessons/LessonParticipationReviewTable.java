package com.aura.details.tables.lessons;

import com.aura.details.tables.base.TenantBaseTable;
import com.aura.details.tables.links.StudentLessonLinkTable;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "LessonParticipationReviews")
public class LessonParticipationReviewTable extends TenantBaseTable {

    @Enumerated(EnumType.STRING)
    private PARTICIPATION_TYPE participationType;

    @OneToOne(fetch = FetchType.LAZY)
    private StudentLessonLinkTable studentLessonLink;

    // TODO: TO fill
    public enum PARTICIPATION_TYPE {
        ATTENTIVE,
        QUIET,
        CURIOUS
    }

}
