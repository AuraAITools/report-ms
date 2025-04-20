package com.reportai.www.reportapi.entities;

import com.reportai.www.reportapi.entities.attachments.StudentLessonRegistration;
import com.reportai.www.reportapi.entities.base.TenantAwareBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import org.hibernate.envers.Audited;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
@Table(name = "LessonTopicProficiencies")
public class LessonTopicProficiency extends TenantAwareBaseEntity {

    @Column
    private Integer rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Topic topic;

    @OneToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private StudentLessonRegistration studentLessonRegistration;

}
