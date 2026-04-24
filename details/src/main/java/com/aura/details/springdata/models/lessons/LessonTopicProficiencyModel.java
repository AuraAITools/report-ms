package com.aura.details.springdata.models.lessons;

import com.aura.details.springdata.models.base.TenantBaseModel;
import com.aura.details.springdata.models.links.StudentLessonLinkModel;
import com.aura.details.springdata.models.topics.TopicModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
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
@Table(name = "LessonTopicProficiencies")
public class LessonTopicProficiencyModel extends TenantBaseModel {

    @Column
    private Integer rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private TopicModel topic;

    @OneToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private StudentLessonLinkModel studentLessonLink;

}
