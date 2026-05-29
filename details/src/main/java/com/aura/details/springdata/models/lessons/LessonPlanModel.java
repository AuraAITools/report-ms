package com.aura.details.springdata.models.lessons;

import com.aura.details.springdata.models.base.TenantBaseModel;
import com.aura.details.springdata.models.links.LessonPlanMaterialsLinkModel;
import com.aura.details.springdata.models.links.LessonPlanTopicLinkModel;
import com.aura.details.springdata.models.links.StudentLessonLinkModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "LessonPlans", uniqueConstraints = {
        @UniqueConstraint(name = "uk_lesson_plans_lesson_id", columnNames = {"lesson_id"})
})
public class LessonPlanModel extends TenantBaseModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private LessonModel lesson;

    @Column(nullable = false, columnDefinition = "TEXT DEFAULT ''")
    private String plan;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private LESSON_PLAN_STATE state = LESSON_PLAN_STATE.UNPLANNED;

    @OneToMany(mappedBy = "lessonPlan", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<StudentLessonLinkModel> registeredStudents;

    @OneToMany(mappedBy = "lessonPlan", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<LessonPlanMaterialsLinkModel> lessonPlanMaterialsLinks = new HashSet<>();

    @OneToMany(mappedBy = "lessonPlan", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<LessonPlanTopicLinkModel> lessonPlanTopicLinks = new HashSet<>();

    @OneToMany(mappedBy = "lessonPlan", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<LessonObjectiveModel> lessonObjectives = new HashSet<>();

    public enum LESSON_PLAN_STATE {
        UNPLANNED,
        DRAFT,
        PLANNED
    }

}
