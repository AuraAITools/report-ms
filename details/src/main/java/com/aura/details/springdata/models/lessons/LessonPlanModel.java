package com.aura.details.springdata.models.lessons;

import com.aura.details.springdata.models.base.TenantBaseModel;
import com.aura.details.springdata.models.links.LessonPlanMaterialsLinkModel;
import com.aura.details.springdata.models.links.LessonPlanTopicLinkModel;
import com.aura.details.springdata.models.links.StudentLessonLinkModel;
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
@Table(name = "LessonPlans")
public class LessonPlanModel extends TenantBaseModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private LessonModel lesson;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean isPlanned;

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

    public enum LESSON_PLAN_STATUS {
        PLANNED,
        UNPLANNED
    }

}
