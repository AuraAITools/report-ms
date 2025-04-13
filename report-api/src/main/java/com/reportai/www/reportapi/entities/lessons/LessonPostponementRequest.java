package com.reportai.www.reportapi.entities.lessons;

import com.reportai.www.reportapi.entities.base.TenantAwareBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "LessonPostponementRequest")
public class LessonPostponementRequest extends TenantAwareBaseEntity {

    public enum APPROVAL_STATUS {
        APPROVED,
        REJECTED,
        PENDING
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Lesson lesson;

    @Column(nullable = false)
    @Builder.Default
    private String reasoning = "";

    @Enumerated(value = EnumType.STRING)
    @Builder.Default
    private APPROVAL_STATUS status = APPROVAL_STATUS.PENDING;


}
