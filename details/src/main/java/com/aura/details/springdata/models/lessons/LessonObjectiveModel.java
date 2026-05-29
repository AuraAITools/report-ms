package com.aura.details.springdata.models.lessons;

import com.aura.details.springdata.models.base.TenantBaseModel;
import com.aura.details.springdata.models.links.LessonObjectiveTopicLinkModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
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
@Table(name = "LessonObjectives")
public class LessonObjectiveModel extends TenantBaseModel {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String objective;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private LessonPlanModel lessonPlan;

    @OneToMany(mappedBy = "lessonObjective", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<LessonObjectiveTopicLinkModel> lessonObjectiveTopicLinks = new HashSet<>();


}
