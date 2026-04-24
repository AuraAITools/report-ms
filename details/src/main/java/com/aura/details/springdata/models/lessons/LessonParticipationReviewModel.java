package com.aura.details.springdata.models.lessons;

import com.aura.details.springdata.models.base.TenantBaseModel;
import com.aura.details.springdata.models.links.StudentLessonLinkModel;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(of = {}, callSuper = true)
@Table(name = "LessonParticipationReviews")
public class LessonParticipationReviewModel extends TenantBaseModel {

    @Enumerated(EnumType.STRING)
    private PARTICIPATION_TYPE participationType;

    @OneToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private StudentLessonLinkModel studentLessonLink;

    // TODO: TO fill
    public enum PARTICIPATION_TYPE {
        ATTENTIVE,
        QUIET,
        CURIOUS
    }

}
