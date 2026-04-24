package com.aura.details.springdata.models.lessons;

import com.aura.details.springdata.models.base.TenantBaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Table(name = "LessonPostponementRequests")
public class LessonPostponementRequestModel extends TenantBaseModel {

    @Column(nullable = false)
    @Builder.Default
    private String reasoning = "";

    @Column(nullable = false)
    private String approvedBy;

    @Enumerated(value = EnumType.STRING)
    @Builder.Default
    private APPROVAL_STATUS status = APPROVAL_STATUS.PENDING;

    @Column(nullable = false)
    private Instant postponedLessonStartTimestamptz;

    @Column(nullable = false)
    private Instant postponedLessonEndTimestamptz;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private LessonModel lesson;


    public enum APPROVAL_STATUS {
        APPROVED,
        REJECTED,
        PENDING
    }

}
