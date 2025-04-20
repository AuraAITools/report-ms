package com.reportai.www.reportapi.entities.lessons;

import com.reportai.www.reportapi.entities.base.TenantAwareBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import org.hibernate.envers.Audited;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Table(name = "LessonCancellationRequests")
public class LessonCancellationRequest extends TenantAwareBaseEntity {

    public enum APPROVAL_STATUS {
        APPROVED,
        REJECTED,
        PENDING
    }

    @Column(nullable = false)
    @Builder.Default
    private String reasoning = "";

    @Column(nullable = false)
    private String approvedBy;

    @Enumerated(value = EnumType.STRING)
    @Builder.Default
    private APPROVAL_STATUS status = APPROVAL_STATUS.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Lesson lesson;

}
