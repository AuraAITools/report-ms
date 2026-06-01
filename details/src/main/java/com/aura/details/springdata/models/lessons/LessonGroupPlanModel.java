package com.aura.details.springdata.models.lessons;

import com.aura.details.springdata.models.base.TenantBaseModel;
import com.aura.details.springdata.models.links.LessonGroupPlanMaterialsLinkModel;
import com.aura.details.springdata.models.links.LessonGroupPlanTopicLinkModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(name = "LessonGroupPlans", uniqueConstraints = {
        @UniqueConstraint(name = "uk_lesson_group_plans_lesson_group_id", columnNames = {"lesson_group_id"})
})
public class LessonGroupPlanModel extends TenantBaseModel {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_group_id", nullable = false)
    @ToString.Exclude
    private LessonGroupModel lessonGroup;

    @Column(nullable = false, columnDefinition = "TEXT DEFAULT ''")
    private String plan;

    @Column(nullable = false, columnDefinition = "TEXT DEFAULT ''")
    private String recap;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private LESSON_GROUP_PLAN_STATE state = LESSON_GROUP_PLAN_STATE.UNPLANNED;

    @OneToMany(mappedBy = "lessonGroupPlan", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<LessonGroupPlanMaterialsLinkModel> lessonGroupPlanMaterialsLinks = new HashSet<>();

    @OneToMany(mappedBy = "lessonGroupPlan", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<LessonGroupPlanTopicLinkModel> lessonGroupPlanTopicLinks = new HashSet<>();

    @OneToMany(mappedBy = "lessonGroupPlan", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<LessonObjectiveModel> lessonObjectives = new HashSet<>();

    @OneToMany(mappedBy = "lessonGroupPlan", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<LessonQuizModel> lessonQuizzes = new HashSet<>();

    public enum LESSON_GROUP_PLAN_STATE {
        UNPLANNED,
        DRAFT,
        PLANNED
    }
}
